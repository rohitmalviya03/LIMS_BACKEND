package com.app.LIMS.master.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.master.entity.SampleMaster;

public interface SampleMasterRepository extends JpaRepository<SampleMaster, Long>  {

}
