package com.app.LIMS.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.app.LIMS.entity.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findBySampleIdAndTestId(Long sampleId, Long testId);
    Optional<Result> findBySample_SampleIdAndTest_IdAndParameter(String sampleId, Long testId, String parameter);
    
    List<Result> findAllBySample_SampleIdAndTest_IdAndValidationStatus(
    	    String sampleId, Long testId, String validationStatus
    	);

    List<Result> findAllByValidationStatus(String validationStatus);
    
    @Query("SELECT r FROM Result r " +
            "WHERE (:entryDate IS NULL OR DATE(r.enterdAt) = :entryDate) " +
            "or r.validationStatus = 'approved'")
     List<Result> searchReports(
         @Param("patientId") String patientId,
         @Param("sampleId") String sampleId,
         @Param("entryDate") LocalDate entryDate
     );

    	


}