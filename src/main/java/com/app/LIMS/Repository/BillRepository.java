package com.app.LIMS.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.LIMS.entity.Bill;
import com.app.LIMS.entity.Patient;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPatient(Patient patient);

	List<Bill> findByPatientAndLabcode(Patient patient, String labcode);
}