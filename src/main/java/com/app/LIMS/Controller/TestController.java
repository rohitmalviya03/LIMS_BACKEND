package com.app.LIMS.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.LIMS.Dto.TestRaiseRequest;
import com.app.LIMS.Repository.PatientRepository;
import com.app.LIMS.Repository.TestSampleRepository;
import com.app.LIMS.Repository.UserRepository;
import com.app.LIMS.Services.TestService;
import com.app.LIMS.entity.Patient;
import com.app.LIMS.entity.User;
import com.app.LIMS.entity.TestSample;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api")
public class TestController {
    @Autowired private PatientRepository patientRepo;
    @Autowired private TestSampleRepository sampleRepo;
    @Autowired private UserRepository userRepo;
    @Autowired
    private TestService testService;

    // 1. Search patient by MRN or name (for auto-fill)
    @GetMapping("/patients/search")
    public ResponseEntity<?> searchPatient(
        @RequestParam(required = false) String mrn,
        @RequestParam(required = false) String name
    ) {
        if (mrn != null) {
            return patientRepo.findByMrn(mrn)
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
        Optional<TestSample> lastSampleOpt = sampleRepo.findTopBySampleNumberStartingWithOrderBySampleNumberDesc(prefix);
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
        sampleRepo.save(sample);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "sampleId", sample.getId(),
            "sampleNumber", sampleNumber
        ));
    }
    
    @GetMapping("/teststatus")
    public List<TestSample> getTests(@RequestParam(value = "status", required = false) String status) {
    	testService.getTestsByStatus(status);
        return testService.getTestsByStatus(status);
    }
    @GetMapping("/tests/{id}")
    public ResponseEntity<TestSample> getTestById(@PathVariable Long id) {
        Optional<TestSample> test = sampleRepo.findById(id);
        User ab= userRepo.findById(test.get().getCreatedBy());
        if(ab!=null) {
        test.get().setCreatebyUser(ab.getUsername());
        }
        else {
        	test.get().setCreatebyUser("NA");
        }
        return test.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    
    @GetMapping("/tests/count")
    public Map<String, Long> getTestCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("all", sampleRepo.count());
        counts.put("pending", sampleRepo.countByStatusIgnoreCase("pending"));
        counts.put("completed", sampleRepo.countByStatusIgnoreCase("completed"));
        counts.put("running", sampleRepo.countByStatusIgnoreCase("running"));
        return counts;
    }
    
}