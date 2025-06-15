package com.app.LIMS.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.LIMS.Repository.PatientRepository;
import com.app.LIMS.entity.Patient;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Value("${labCode}")
    private String labCode;
    public List<Patient> getAllPatients() {
        return patientRepository.findByLabcode(labCode);
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient createPatient(Patient patient) {
    	
    	patient.setLabcode(labCode);
        if (patient.getMrn() != null && patientRepository.existsByMrn(patient.getMrn()))
            throw new RuntimeException("MRN already exists");
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient updated) {

    	updated.setLabcode(labCode);
        return patientRepository.findById(id).map(patient -> {
            patient.setFirstName(updated.getFirstName());
            patient.setLastName(updated.getLastName());
            patient.setGender(updated.getGender());
            patient.setDob(updated.getDob());
            patient.setContact(updated.getContact());
            patient.setEmail(updated.getEmail());
            patient.setAddress(updated.getAddress());
            patient.setIdProof(updated.getIdProof());
            patient.setMrn(updated.getMrn());
            patient.setEmergencyContact(updated.getEmergencyContact());
            patient.setStatus(updated.getStatus());
            return patientRepository.save(patient);
        }).orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public long countPatients() {
        return patientRepository.count();
    }
}