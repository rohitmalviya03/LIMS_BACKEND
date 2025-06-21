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
//
//    public Patient createPatient(Patient patient) {
//    	
//    	//patient.setLabcode(labCode);
//        if (patient.getMrn() != null && patientRepository.existsByMrn(patient.getMrn()))
//            throw new RuntimeException("MRN already exists");
//        return patientRepository.save(patient);
//    }
//
//    public Patient updatePatient(Long id, Patient updated) {
//
//    	updated.setLabcode(labCode);
//        return patientRepository.findById(id).map(patient -> {
//            patient.setFirstName(updated.getFirstName());
//            patient.setLastName(updated.getLastName());
//            patient.setGender(updated.getGender());
//            patient.setDob(updated.getDob());
//            patient.setContact(updated.getContact());
//            patient.setEmail(updated.getEmail());
//            patient.setAddress(updated.getAddress());
//            patient.setIdProof(updated.getIdProof());
//            patient.setMrn(updated.getMrn());
//            patient.setEmergencyContact(updated.getEmergencyContact());
//            patient.setStatus(updated.getStatus());
//            return patientRepository.save(patient);
//        }).orElseThrow(() -> new RuntimeException("Patient not found"));
//    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public long countPatients() {
        return patientRepository.count();
    }


    // Get all patients for a labcode
    public List<Patient> getAllPatientsByLabcode(String labcode) {
        return patientRepository.findAllByLabcode(labcode);
    }

    // Get patient by id and labcode
    public Optional<Patient> getPatientByIdAndLabcode(Long id, String labcode) {
        return patientRepository.findByIdAndLabcode(id, labcode);
    }

    // Create patient (labcode should be set in Patient object)
    public Patient createPatient(Patient patient) {
        if (patient.getLabcode() == null) throw new RuntimeException("Labcode required");
        Integer maxMrn = patientRepository.findMaxMrnByLabcode(patient.getLabcode());
        int nextMrn = (maxMrn == null) ? 1 : maxMrn + 1;
        patient.setMrn(String.valueOf(nextMrn));
        return patientRepository.save(patient);
    }

    // Update patient (labcode should be set in Patient object)
    public Patient updatePatient(Long id, Patient updatedPatient) {
        Patient patient = patientRepository.findByIdAndLabcode(id, updatedPatient.getLabcode())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        // Update fields
        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setGender(updatedPatient.getGender());
        patient.setDob(updatedPatient.getDob());
        patient.setContact(updatedPatient.getContact());
        patient.setEmail(updatedPatient.getEmail());
        patient.setAddress(updatedPatient.getAddress());
        patient.setIdProof(updatedPatient.getIdProof());
        patient.setMrn(updatedPatient.getMrn());
        patient.setEmergencyContact(updatedPatient.getEmergencyContact());
        // ...add any other fields as needed
        return patientRepository.save(patient);
    }

    // Delete patient by id and labcode
    public void deletePatientByIdAndLabcode(Long id, String labcode) {
        patientRepository.deleteByIdAndLabcode(id, labcode);
    }

    // Count patients for a labcode
    public long countPatientsByLabcode(String labcode) {
        return patientRepository.countByLabcode(labcode);
    }

}