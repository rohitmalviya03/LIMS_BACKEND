package com.app.LIMS.master.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.master.entity.TestMaster;

public interface TestMasterRepository extends JpaRepository<TestMaster, Long> {
    List<TestMaster> findAll();
    
    Optional<TestMaster> findByTestNameIgnoreCase(String testName);

	boolean existsByTestNameAndSampleType(String testName, String sampleType);

	List<TestMaster> findAllByLabcode(String labcode);

	boolean existsByTestNameAndSampleTypeAndLabcode(String testName, String sampleType, String labcode);



}