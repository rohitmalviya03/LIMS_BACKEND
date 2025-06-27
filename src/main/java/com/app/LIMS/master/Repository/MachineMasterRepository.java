package com.app.LIMS.master.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.master.entity.MachineMaster;

public interface MachineMasterRepository extends JpaRepository<MachineMaster, Long>  {

	List<MachineMaster> findAllByLabcode(String labcode);

	boolean existsByNameAndLabcode(String name, String labcode);

}
