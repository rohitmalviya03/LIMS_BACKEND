package com.app.LIMS.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.app.LIMS.Repository.TestSampleRepository;
import com.app.LIMS.Services.SampleService;
import com.app.LIMS.entity.Sample;
import com.app.LIMS.entity.TestSample;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/samples")
@CrossOrigin
public class SampleController {

    @Autowired
    private SampleService sampleService;
    @Autowired
    private TestSampleRepository sampleRepo;

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
}