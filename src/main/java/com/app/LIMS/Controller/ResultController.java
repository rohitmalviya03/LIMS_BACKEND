package com.app.LIMS.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.LIMS.Repository.ResultRepository;
import com.app.LIMS.Repository.SampleRepository;
import com.app.LIMS.Repository.TestSampleRepository;
import com.app.LIMS.entity.Result;
import com.app.LIMS.entity.Sample;
import com.app.LIMS.entity.TestSample;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private SampleRepository sampleRepository;
    @Autowired
    private TestSampleRepository testRepository;

    // DTO for UI entry
    public static class EntryRequest {
        public String sampleId;
        public Map<String, String> results;
        public String userId; // Add this field
    }

    @PostMapping("/entry")
    public ResponseEntity<?> saveResults(@RequestBody EntryRequest req) {
        if (req.sampleId == null || req.results == null) {
            return ResponseEntity.badRequest().body("Missing data");
        }
        Optional<Sample> sampleOpt = sampleRepository.findBySampleId(req.sampleId);
        if (!sampleOpt.isPresent()) return ResponseEntity.badRequest().body("Sample not found");

        req.results.forEach((key, value) -> {
            String[] parts = key.split("_", 2);
            Long testId;
            String parameter = null;
            try {
                testId = Long.parseLong(parts[0]);
                if (parts.length > 1) parameter = parts[1];
            } catch (NumberFormatException e) {
                return;
            }
            Optional<TestSample> testOpt = testRepository.findById(testId);
            if (testOpt.isPresent()) {
            	Result result = resultRepository.findBySample_SampleIdAndTest_IdAndParameter(req.sampleId, testId,parameter)
            		    .orElse(new Result());
                result.setSample(sampleOpt.get());
                result.setTest(testOpt.get());
                result.setValue(value);
                result.setParameter(parameter); // Make sure your Result entity has a 'parameter' field
                result.setEnterdAt(LocalDateTime.now());
                result.setEnteredBy(req.userId);
                result.setValidationStatus("pending");
                
                
                resultRepository.save(result);
            }
        });

        // Optionally update sample status
        Sample sample = sampleOpt.get();
        sample.setStatus("result_entered");
        sampleRepository.save(sample);

        return ResponseEntity.ok().body("Results saved");
    }
    
    
    @GetMapping("/pending-validation")
    public List<ResultDTO> getPendingResults() {
        List<Result> results = resultRepository.findAllByValidationStatus("pending");
        return results.stream().map(ResultDTO::fromEntity).collect(Collectors.toList());
    }
    public static class ResultDTO {
        public Long id;
        public String sampleId;
        public String testName;
        public String parameter;
        public String value;
        public String enteredBy;
        public LocalDateTime enteredAt;
        public String validationStatus;
        public String validatedBy;
        public LocalDateTime validatedAt;

        public static ResultDTO fromEntity(Result r) {
            ResultDTO dto = new ResultDTO();
            dto.id = r.getId();
            dto.sampleId = r.getSample().getSampleId();
            dto.testName = r.getTest().getTestName();
            dto.parameter = r.getParameter();
            dto.value = r.getValue();
            dto.enteredBy = r.getEnteredBy();
            dto.enteredAt = r.getEnterdAt();
            dto.validationStatus = r.getValidationStatus();
            dto.validatedBy = r.getValidatedBy();
            dto.validatedAt = r.getValidatedAt();
            return dto;
        }
    }

    // Validate (approve/reject) a result
    public static class ValidateRequest {
        public Long resultId;
        public String status; // "approved" or "rejected"
        public String doctorId;
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateResult(@RequestBody ValidateRequest req) {
        Optional<Result> resultOpt = resultRepository.findById(req.resultId);
        if (!resultOpt.isPresent()) return ResponseEntity.badRequest().body("Result not found");
        Result result = resultOpt.get();
        result.setValidationStatus(req.status);
        result.setValidatedBy(req.doctorId);
        result.setValidatedAt(LocalDateTime.now());
        resultRepository.save(result);
        return ResponseEntity.ok().body("Result " + req.status);
    }
    
    
    @GetMapping("/report")
    public ResponseEntity<?> getReport(
        @RequestParam String sampleId,
        @RequestParam Long testId
    ) {
        // Fetch only approved results for this sample and test
        List<Result> results = resultRepository
            .findAllBySample_SampleIdAndTest_IdAndValidationStatus(
                sampleId, testId, "approved"
            );
        if (results.isEmpty()) {
            return ResponseEntity.badRequest().body("No approved results found.");
        }
        // You can map to a DTO if you want to hide internal fields
        return ResponseEntity.ok(results);
    }
    
    
    
    @GetMapping("/report/search")
    public List<ReportSearchDTO> searchReports(
        @RequestParam(required = false) String patientId,
        @RequestParam(required = false) String sampleId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entryDate
    ) {
        List<Result> results = resultRepository.searchReports(patientId, sampleId, entryDate);
        return results.stream().map(ReportSearchDTO::fromEntity).collect(Collectors.toList());
    }

    // DTO for search results
    public static class ReportSearchDTO {
        public Long id;
        public String sampleId;
        public String testName;
        public Long patientId;
        public String patName;
        
        public String resEnteredAt;
        public String validatedAt;
        public Long testId;

        public static ReportSearchDTO fromEntity(Result r) {
            ReportSearchDTO dto = new ReportSearchDTO();
            dto.id = r.getId();
            dto.sampleId = r.getSample().getSampleId();
            dto.testName = r.getTest().getTestName();
            dto.patientId = r.getSample().getPatientId(); // <-- get from Sample
            dto.patName=r.getSample().getPatientName();
            dto.resEnteredAt = r.getEnterdAt() != null ? String.valueOf(r.getEnterdAt()) : null;
            dto.validatedAt = r.getValidatedAt() != null ? String.valueOf(r.getValidatedAt()) : null;
            
            dto.testId = r.getTest().getId();
            return dto;
        }
    }

}