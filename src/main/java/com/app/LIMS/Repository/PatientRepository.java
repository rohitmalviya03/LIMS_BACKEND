
package com.app.LIMS.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByMrn(String mrn);
    long count();
    Optional<Patient> findByMrn(String mrn);
    
 
   
}