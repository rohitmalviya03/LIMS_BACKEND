package com.app.LIMS.master.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.master.entity.TestMaster;

public interface TestMasterRepository extends JpaRepository<TestMaster, Long> {
    List<TestMaster> findAll();
    
    Optional<TestMaster> findByTestNameIgnoreCase(String testName);
}