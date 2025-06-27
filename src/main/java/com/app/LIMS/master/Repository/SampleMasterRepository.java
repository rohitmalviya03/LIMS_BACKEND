package com.app.LIMS.master.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.master.entity.SampleMaster;

public interface SampleMasterRepository extends JpaRepository<SampleMaster, Long>  {

	List<SampleMaster> findAllByLabcode(String labcode);

	boolean existsByTypeAndLabcode(String labcode, String type);

}
