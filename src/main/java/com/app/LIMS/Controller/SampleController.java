package com.app.LIMS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.app.LIMS.Repository.SampleRepository;
import com.app.LIMS.Repository.TestSampleRepository;
import com.app.LIMS.Services.SampleService;
import com.app.LIMS.entity.Sample;
import com.app.LIMS.entity.TestSample;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/samples")

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class SampleController {

    @Autowired
    private SampleService sampleService;
    @Autowired
    private TestSampleRepository sampleRepo;
    @Autowired
    private SampleRepository sampletesrepo;

    @GetMapping("/pending")
    public List<TestSample> getPendingSamples() {
    	
    	return sampleRepo.findByStatusIgnoreCase("Pending");
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
        String collectedAtStr = (String) req.get("collectedAt");
        LocalDateTime collectedAt = LocalDateTime.parse(collectedAtStr);
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
    
    
    
    @PutMapping("/{sampleId}/status")
    public Sample updateSampleStatus(
            @PathVariable Long sampleId,
            @RequestBody StatusUpdateRequest request
    ) {
        return sampleService.updateSampleStatus(sampleId, request.getStatus());
    }

    // DTO for status update
    public static class StatusUpdateRequest {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    @GetMapping("/count")
    public  Map<String, Long> getTestCounts() {
       // Map<String, Long> counts = new HashMap<>();
    	Map<String, Long> counts = new HashMap<>();
        counts.put("all", sampletesrepo.count());
        counts.put("pending", sampletesrepo.countByStatusIgnoreCase("pending"));
        counts.put("completed", sampletesrepo.countByStatusIgnoreCase("completed"));
        counts.put("collected", sampletesrepo.countByStatusIgnoreCase("collected"));
        counts.put("running", sampletesrepo.countByStatusIgnoreCase("running"));
        return counts;
    }
}