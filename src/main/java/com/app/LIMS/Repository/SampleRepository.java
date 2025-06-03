package com.app.LIMS.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.entity.Sample;

public interface SampleRepository extends JpaRepository<Sample, Long> {
    List<Sample> findByStatus(String status);
    Optional<Sample> findBySampleId(String sampleId);
}