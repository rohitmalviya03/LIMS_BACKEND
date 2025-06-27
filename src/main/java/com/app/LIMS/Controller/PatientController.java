package com.app.LIMS.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.LIMS.Services.PatientService;
import com.app.LIMS.entity.Patient;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Get all patients for a labcode
    @GetMapping
    public List<Patient> getAllPatients(@RequestParam String labcode) {
        return patientService.getAllPatientsByLabcode(labcode);
    }

    // Get patient by id and labcode
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id, @RequestParam String labcode) {
        Optional<Patient> patient = patientService.getPatientByIdAndLabcode(id, labcode);
        return patient.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Create patient (labcode in body)
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        try {
            return ResponseEntity.ok(patientService.createPatient(patient));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update patient (labcode in body)
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        try {
            return ResponseEntity.ok(patientService.updatePatient(id, patient));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete patient by id and labcode
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id, @RequestParam String labcode) {
        patientService.deletePatientByIdAndLabcode(id, labcode);
        return ResponseEntity.noContent().build();
    }

    // Count patients for a labcode
    @GetMapping("/count")
    public long countPatients(@RequestParam String labcode) {
        return patientService.countPatientsByLabcode(labcode);
    }
}