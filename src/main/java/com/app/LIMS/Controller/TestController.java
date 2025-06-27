package com.app.LIMS.Controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.LIMS.Dto.TestRaiseRequest;
import com.app.LIMS.Repository.PatientRepository;
import com.app.LIMS.Repository.SampleRepository;
import com.app.LIMS.Repository.TestSampleRepository;
import com.app.LIMS.Repository.UserRepository;
import com.app.LIMS.Services.TestService;
import com.app.LIMS.entity.Patient;
import com.app.LIMS.entity.Sample;
import com.app.LIMS.entity.User;
import com.app.LIMS.entity.TestSample;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired private PatientRepository patientRepo;
    @Autowired private TestSampleRepository testRepo;
    @Autowired private UserRepository userRepo;
    
    @Autowired SampleRepository sampleRepo;

	 @Value("${labCode}")
	 private String labCode;
    @Autowired
    private TestService testService;

    // 1. Search patient by MRN or name (for auto-fill)
    @GetMapping("/patients/search")
    public ResponseEntity<?> searchPatient(
        @RequestParam(required = false) String mrn,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String labcode
    ) {
        if (mrn != null) {
            return patientRepo.findByMrnAndLabcode(mrn,labcode)
                .map(ResponseEntity::ok) 
                .orElseGet(() -> ResponseEntity.notFound().build());
        } 
        return ResponseEntity.badRequest().body("Provide mrn or name");
    }

    // 2. Raise a test for a patient
    @PostMapping("/addtests")
    public ResponseEntity<?> raiseTest(@RequestBody TestRaiseRequest req) {
        Optional<Patient> patientOpt = patientRepo.findById(req.getPatientId());
        if (!patientOpt.isPresent()) return ResponseEntity.status(404).body("Patient not found");

        // Generate sample number
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "LAB-" + dateStr + "-";
        Optional<TestSample> lastSampleOpt = testRepo.findTopBySampleNumberStartingWithOrderBySampleNumberDesc(prefix);
        int nextNum = 1;
        if (lastSampleOpt.isPresent()) {
            String lastSampleNum = lastSampleOpt.get().getSampleNumber();
            String[] parts = lastSampleNum.split("-");
            nextNum = Integer.parseInt(parts[2]) + 1;
        }
        String sampleNumber = prefix + String.format("%04d", nextNum);

        TestSample sample = new TestSample();
        sample.setSampleNumber(sampleNumber);
        sample.setPatient(patientOpt.get());
        sample.setTestName(req.getTestName());
        sample.setCreatedBy(req.getTestRaisedBy());
        sample.setNotes(req.getNotes());
        sample.setStatus("Pending");
        sample.setCreatedAt(LocalDateTime.now());
        testRepo.save(sample);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "sampleId", sample.getId(),
            "sampleNumber", sampleNumber
        ));
    }
    
    @GetMapping("/teststatus")
    public List<TestSample> getTests(@RequestParam(value = "status", required = false) String status) {
    	List<TestSample> li=testService.getTestsByStatus(status);
        return testService.getTestsByStatus(status);
    }
    @GetMapping("/tests/{id}")
    public ResponseEntity<TestSample> getTestById(@PathVariable Long id,@RequestParam String labcode) {
        Optional<TestSample> test = testRepo.findByIdAndLabcode(id,labcode);
        User ab= userRepo.findByIdAndLabcode(test.get().getCreatedBy(),labcode);
        if(ab!=null) {
        test.get().setCreatebyUser(ab.getUsername());
        }
        else {
        	test.get().setCreatebyUser("NA");
        }
        return test.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    
    @GetMapping("/tests/count")
    public Map<String, Long> getTestCounts(@RequestParam String labcode) {
        Map<String, Long> counts = new HashMap<>();
        counts.put("all", testRepo.countByLabcode(labcode));
        counts.put("pending", testRepo.countByStatusIgnoreCase("pending"));
        counts.put("completed", testRepo.countByStatusIgnoreCase("completed"));
        counts.put("collected", testRepo.countByStatusIgnoreCase("collected"));
        counts.put("running", testRepo.countByStatusIgnoreCase("running"));
        return counts;
    }
    
    @PostMapping("/addtests/bulk")
    public ResponseEntity<?> raiseBulkTests(@RequestBody TestRaiseRequest req) {
        Optional<Patient> patientOpt = patientRepo.findByIdAndLabcode(req.getPatientId(), req.labcode);
        if (!patientOpt.isPresent()) return ResponseEntity.status(404).body("Patient not found");

        // Prepare for sample number generation
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "LAB-" + dateStr + "-";

        // Find the last sample number for today
        Optional<TestSample> lastSampleOpt = testRepo.findTopBySampleNumberStartingWithOrderBySampleNumberDesc(prefix);
        int nextNum = 1;
        if (lastSampleOpt.isPresent()) {
            String lastSampleNum = lastSampleOpt.get().getSampleNumber();
            String[] parts = lastSampleNum.split("-");
            nextNum = Integer.parseInt(parts[2]) + 1;
        }

        List<String> sampleNumbers = new ArrayList<>();
        List<Long> sampleIds = new ArrayList<>();

        for (TestRaiseRequest.TestItem testItem : req.getTests()) {
            String sampleNumber = prefix + String.format("%04d", nextNum++);

            TestSample sample = new TestSample();
            Sample sC = new Sample();
            sample.setSampleNumber(sampleNumber);
            sample.setPatient(patientOpt.get());
            sample.setTestId(Long.parseLong(testItem.getTestId()));
            sample.setCreatedBy(req.getTestRaisedBy());
            sample.setNotes(req.getNotes());
            sample.setStatus("Pending");
            sample.setCreatedAt(LocalDateTime.now());
            sample.setLabcode(req.labcode);
            testRepo.save(sample);

            
            sC.setPatientId(patientOpt.get().getId());
            sC.setPatientName(patientOpt.get().getFirstName());
            sC.setSampleId(sampleNumber);
            sC.setCollector(String.valueOf(req.getTestRaisedBy()));
            sC.setTests(testItem.getTestId());
            sC.setStatus("Pending");
            sC.setLabcode(req.labcode);
            sampleRepo.save(sC);
            sampleNumbers.add(sampleNumber);
            sampleIds.add(sample.getId());
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "sampleNumbers", sampleNumbers,
            "sampleIds", sampleIds
        ));
    }
    
	/*
	 * @PutMapping("/raisedtests/{id}") public ResponseEntity<?> editRaisedTest(
	 * 
	 * @PathVariable Long id,
	 * 
	 * @RequestBody TestRaiseRequest req) { Optional<TestSample> sampleOpt =
	 * testRepo.findById(id); if (sampleOpt.isEmpty()) return
	 * ResponseEntity.status(404).body("Sample not found");
	 * 
	 * TestSample sample = sampleOpt.get();
	 * 
	 * if (req.getTestName() != null) sample.setTestName(req.getTestName()); if
	 * (req.getNotes() != null) sample.setNotes(req.getNotes()); // Add more fields
	 * as needed (status, etc.)
	 * 
	 * testRepo.save(sample);
	 * 
	 * return ResponseEntity.ok(Map.of( "success", true, "message",
	 * "Test updated successfully" )); }
	 */
    @GetMapping("/raisedtests")
    public ResponseEntity<?> getRaisedTests(@RequestParam Long patientId,@RequestParam String labcode) {
        List<TestSample> list = testRepo.findByPatientIdAndLabcode(patientId,labcode);
        return ResponseEntity.ok(list);
    }

    // Edit a raised test (test name, notes)
    @PutMapping("/raisedtests/{id}")
    public ResponseEntity<?> editRaisedTest(@PathVariable Long id,@RequestParam String labcode ,@RequestBody TestRaiseRequest req) {
        Optional<TestSample> sampleOpt = testRepo.findByIdAndLabcode(id,labcode);
        if (sampleOpt.isEmpty()) return ResponseEntity.status(404).body("Sample not found");

        TestSample sample = sampleOpt.get();
        if (req.getTestName() != null && !req.getTestName().trim().isEmpty()) sample.setTestName(req.getTestName());
        if (req.getNotes() != null) sample.setNotes(req.getNotes());
       // sample.setUpdatedAt(LocalDateTime.now());
        testRepo.save(sample);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Test updated successfully"
        ));
    }
}