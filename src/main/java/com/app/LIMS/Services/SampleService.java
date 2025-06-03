package com.app.LIMS.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.LIMS.Repository.SampleRepository;
import com.app.LIMS.Repository.TestSampleRepository;
import com.app.LIMS.entity.Sample;
import com.app.LIMS.entity.TestSample;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class SampleService {

    @Autowired
    private SampleRepository sampleRepository;
    @Autowired
    private TestSampleRepository testrepo;
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
        
        
        TestSample testSam= testrepo.findBySampleNumber(sampleId);
        testSam.setStatus("collected");
        testrepo.save(testSam);
        
        return sampleRepository.save(sample);
    }

    public Sample createSample(Sample sample) {
        sample.setStatus("pending");
        return sampleRepository.save(sample);
    }
    

    public List<Sample> getCollectedSamples(LocalDate date, String status) {
        if (date != null && status != null && !status.equalsIgnoreCase("All")) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(LocalTime.MAX);
            return sampleRepository.findAll().stream()
                .filter(s -> s.getStatus().equalsIgnoreCase(status))
                .filter(s -> s.getCollectedAt() != null &&
                    !s.getCollectedAt().isBefore(start) &&
                    !s.getCollectedAt().isAfter(end))
                .toList();
        } else if (date != null) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(LocalTime.MAX);
            return sampleRepository.findAll().stream()
                .filter(s -> s.getStatus().equalsIgnoreCase("Collected"))
                .filter(s -> s.getCollectedAt() != null &&
                    !s.getCollectedAt().isBefore(start) &&
                    !s.getCollectedAt().isAfter(end))
                .toList();
        } else if (status != null && !status.equalsIgnoreCase("All")) {
            return sampleRepository.findByStatus(status);
        } else {
            return sampleRepository.findByStatus("Collected");
        }
    }
    
    
    public Sample updateSampleStatus(Long sampleId, String status) {
        Optional<Sample> optionalSample = sampleRepository.findById(sampleId);
        if (optionalSample.isPresent()) {
            Sample sample = optionalSample.get();
            sample.setStatus(status);
            return sampleRepository.save(sample);
        } else {
            throw new RuntimeException("Sample not found");
        }
    }
}