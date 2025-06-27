package com.app.LIMS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.LIMS.Repository.SampleRepository;
import com.app.LIMS.Repository.TestSampleRepository;
import com.app.LIMS.Services.SampleService;
import com.app.LIMS.entity.Sample;
import com.app.LIMS.entity.TestSample;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/samples")

public class SampleController {

    @Autowired
    private SampleService sampleService;
    @Autowired
    private TestSampleRepository sampleRepo;
    @Autowired
    private SampleRepository sampletesrepo;

    @GetMapping("/pending")
    public List<TestSample> getPendingSamples(@RequestParam String labcode) {
    	 List<String> statuses = List.of("Pending", "Re-collect");
    	    return sampleRepo.findByStatusInAndLabcodeIgnoreCase(statuses, labcode);
       // return sampleService.getPendingSamples();
    }

    @GetMapping("/{sampleId}")
    public Sample getSample(@PathVariable String sampleId) {
        return sampleService.getSampleBySampleId(sampleId)
                .orElseThrow(() -> new RuntimeException("Sample not found"));
    }

    @PostMapping("/collect")
    public Sample collectSample(@RequestBody Map<String, Object> req) {
        String sampleId = (String) req.get("sampleId");
        String collector = (String) req.get("collector");
       // String collectedAtStr = (String) req.get("collectedAt");
        LocalDateTime collectedAt =null;// LocalDateTime.parse(collectedAtStr);
        return sampleService.collectSample(sampleId, collector, collectedAt);
    }

    @PostMapping
    public Sample createSample(@RequestBody Sample sample) {
        return sampleService.createSample(sample);
    }
    

    @GetMapping("/collected")
    public List<Sample> getCollectedSamples(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String status
    ) {
        return sampleService.getCollectedSamples(date, status);
    }
    
    @GetMapping("/processed")
    public List<Sample> getProcessedSamples() {
        return sampletesrepo.findByStatus("processed");
    }
    
 
    @PutMapping("/status")
    public Sample updateSampleStatus(@RequestBody StatusUpdateRequest request) {
        return sampleService.updateSampleStatus(request.getSampleId(), request.getStatus());
    }

    
    
    // DTO for status update
    public static class StatusUpdateRequest {
    	  private String sampleId;  // Add this field
    	    public String getSampleId() {
			return sampleId;
		}
		public void setSampleId(String sampleId) {
			this.sampleId = sampleId;
		}
			private String status;
       
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    @GetMapping("/count")
    public  Map<String, Long> getTestCounts(@RequestParam String labcode) {
       // Map<String, Long> counts = new HashMap<>();
    	Map<String, Long> counts = new HashMap<>();
        counts.put("all", sampletesrepo.countByLabcode(labcode));
        counts.put("pending", sampletesrepo.countByStatusIgnoreCase("pending"));
        counts.put("completed", sampletesrepo.countByStatusIgnoreCase("completed"));
        counts.put("collected", sampletesrepo.countByStatusIgnoreCase("collected"));
        counts.put("running", sampletesrepo.countByStatusIgnoreCase("running"));
        return counts;
    }
    
 // Get tests for a sample (for result entry)
    @GetMapping("/{id}/tests")
    public List<TestSample> getTestsForSample(@PathVariable Long id) {
        Optional<Sample> sampleOpt = sampletesrepo.findById(id);
        if (sampleOpt.isPresent()) {
            Sample sample = sampleOpt.get();
            if (sample.getTests() != null && !sample.getTests().isEmpty()) {
                List<Long> testIds = java.util.Arrays.stream(sample.getTests().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .toList();
                return sampleRepo.findAllById(testIds);
            }
        }
        return List.of();
    }
    
 // Get sample by sample number (for result entry search)
    @GetMapping("/by-number/{sampleNumber}")
    public ResponseEntity<?> getSampleByNumber(@PathVariable String sampleNumber) {
        Optional<Sample> sampleOpt = sampletesrepo.findBySampleId(sampleNumber);
        return sampleOpt.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}