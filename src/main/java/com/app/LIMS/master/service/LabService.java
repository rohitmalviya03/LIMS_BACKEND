package com.app.LIMS.master.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.LIMS.master.Repository.LabRepository;
import com.app.LIMS.master.dto.LabRegistrationRequest;
import com.app.LIMS.master.dto.LabRegistrationResponse;
import com.app.LIMS.master.entity.Lab;


@Service
public class LabService {
    @Autowired
    private LabRepository labRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public LabRegistrationResponse  registerLab(LabRegistrationRequest request) {
        if (labRepository.findByLabCode(request.getLabCode()).isPresent()) {
        	
            throw new RuntimeException("Lab code already exists.");
        
        
        }
        Lab lab = new Lab();
        lab.setName(request.getName());
        lab.setLabCode(request.getLabCode());
        lab.setDescription(request.getDescription());
        lab.setLocation(request.getLocation());
        lab.setLogo(request.getLogo());
        lab.setMode(request.getMode());
        lab.setStatus(request.getStatus());
        lab.setPincode(request.getPincode());
        
        lab.setUserid(request.getLabCode());
        String encodedPassword = passwordEncoder.encode("123456");
        lab.setPassword(encodedPassword);
    	labRepository.save(lab);
    	 return new LabRegistrationResponse(lab.getLabCode(), "123456");   		
        	
    }
    
    

    // Get current lab (you may use authentication context to get current lab)
    public Lab getCurrentLab() {
        // Example: fetch lab by current user's labId
        // Replace with actual authentication logic
        Long labId = getCurrentLabIdFromAuth();
        return labRepository.findById(labId).orElse(null);
    }

    // Update lab details
    public Lab updateLab(Lab updatedLab) {
        Lab lab = labRepository.findById(updatedLab.getId()).orElse(null);
        if (lab != null) {
            lab.setName(updatedLab.getName());
            lab.setDescription(updatedLab.getDescription());
            lab.setLocation(updatedLab.getLocation());
            lab.setLogo(updatedLab.getLogo());
            lab.setMode(updatedLab.getMode());
            lab.setStatus(updatedLab.getStatus());
            lab.setPincode(updatedLab.getPincode());
            // ...update other fields as needed
            return labRepository.save(lab);
        }
        return null;
    }

    // Helper: get current labId from authentication (stub)
    private Long getCurrentLabIdFromAuth() {
        // TODO: Implement logic to get current labId from security context
        return 1L; // For demo, always return 1
    }
    
    // Get a Lab by ID
    public Lab getLabById(Long id) {
        Optional<Lab> lab = labRepository.findById(id);
        return lab.orElse(null); // Return null if Lab is not found
    }

    // Update an existing Lab
    public LabRegistrationResponse updateLab(Long id, LabRegistrationRequest request) {
        Optional<Lab> optionalLab = labRepository.findById(id);
        if (!optionalLab.isPresent()) {
            return null; // Lab not found
        }

        Lab lab = optionalLab.get();
        lab.setLabCode(request.getLabCode());
        lab.setName(request.getName());
      //  lab.setAddress(request.getAddress());
        // Update other fields from request

        lab = labRepository.save(lab);
        return new LabRegistrationResponse(lab.getLabCode(), lab.getName());
    }

    // Delete a Lab by ID
    public boolean deleteLab(Long id) {
        Optional<Lab> optionalLab = labRepository.findById(id);
        if (!optionalLab.isPresent()) {
            return false; // Lab not found
        }
        labRepository.delete(optionalLab.get());
        return true; // Successfully deleted
    }
    
    public List<Map<String, String>> getAllLabs() {
        // Get all labs from the database
        List<Lab> labs = labRepository.findAll();
        
        // Create a list of maps where each map represents a lab's labCode and name
        return labs.stream()
                   .map(lab -> Map.of(
                       "labCode", lab.getLabCode(),
"id",String.valueOf(lab.getId()),
                       "name", lab.getName()))
                   .collect(Collectors.toList());
    }
}