package com.app.LIMS.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.entity.TestSample;

public interface TestSampleRepository extends JpaRepository<TestSample, Long> {
    Optional<TestSample> findTopByOrderByIdDesc();
    Optional<TestSample> findTopBySampleNumberStartingWithOrderBySampleNumberDesc(String prefix);
    List<TestSample> findByStatusIgnoreCase(String status);
    long countByStatusIgnoreCase(String status);
	List<TestSample> findByPatientId(Long patientId);
}