package com.app.LIMS.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.LIMS.Repository.SampleRepository;
import com.app.LIMS.Repository.TestSampleRepository;
import com.app.LIMS.entity.Sample;

import java.util.List;
import java.util.Optional;

@Service
public class SampleService {

    @Autowired
    private SampleRepository sampleRepository;
    @Autowired
    private TestSampleRepository sampleRepo;
    public List<Sample> getPendingSamples() {
        return sampleRepository.findByStatus("pending");
    }

    public Optional<Sample> getSampleBySampleId(String sampleId) {
        return sampleRepository.findBySampleId(sampleId);
    }

    public Sample collectSample(String sampleId, String collector, java.time.LocalDateTime collectedAt) {
        Sample sample = sampleRepository.findBySampleId(sampleId)
            .orElseThrow(() -> new IllegalArgumentException("Sample not found"));
        sample.setStatus("collected");
        sample.setCollector(collector);
        sample.setCollectedAt(collectedAt);
        return sampleRepository.save(sample);
    }

    public Sample createSample(Sample sample) {
        sample.setStatus("pending");
        return sampleRepository.save(sample);
    }
}