package com.app.LIMS.master.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.master.entity.Lab;

import java.util.Optional;

public interface LabRepository extends JpaRepository<Lab, Long> {
    Optional<Lab> findByLabCode(String labCode);
}