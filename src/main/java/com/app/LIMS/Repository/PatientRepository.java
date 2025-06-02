package com.app.LIMS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByMrn(String mrn);
    long count();
}