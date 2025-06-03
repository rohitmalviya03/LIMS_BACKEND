package com.app.LIMS.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.LIMS.entity.Sample;

public interface SampleRepository extends JpaRepository<Sample, Long> {
    List<Sample> findByStatus(String status);
    Optional<Sample> findBySampleId(String sampleId);
    
    // Find all collected samples for a specific date
    @Query("SELECT s FROM Sample s WHERE s.status = 'Collected' AND DATE(s.collectedAt) = DATE(:date)")
    List<Sample> findCollectedByDate(LocalDateTime date);

    // Find all collected samples for a specific date and status
    @Query("SELECT s FROM Sample s WHERE (:status IS NULL OR s.status = :status) AND DATE(s.collectedAt) = DATE(:date)")
    List<Sample> findByStatusAndDate(String status, LocalDateTime date);
    long countByStatusIgnoreCase(String status);
}