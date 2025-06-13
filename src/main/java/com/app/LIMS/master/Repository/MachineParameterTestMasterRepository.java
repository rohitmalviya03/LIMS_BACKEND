package com.app.LIMS.master.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.master.entity.MachineParameterTestMaster;

public interface MachineParameterTestMasterRepository extends JpaRepository<MachineParameterTestMaster, Long> {
	
	   List<MachineParameterTestMaster> findByTestId(Long testId);
} 