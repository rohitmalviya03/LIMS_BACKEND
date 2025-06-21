
package com.app.LIMS.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.LIMS.entity.Patient;


public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByMrn(String mrn);
    long count();
    Optional<Patient> findByMrn(String mrn);
    
    List<Patient> findByLabcode(String labCode);
	List<Patient> findAllByLabcode(String labcode);
	Optional<Patient> findByIdAndLabcode(Long id, String labcode);
	void deleteByIdAndLabcode(Long id, String labcode);
	long countByLabcode(String labcode);
	@Query("SELECT MAX(p.mrn) FROM Patient p WHERE p.labcode = :labcode")
	Integer findMaxMrnByLabcode(@Param("labcode") String labcode);
   
}