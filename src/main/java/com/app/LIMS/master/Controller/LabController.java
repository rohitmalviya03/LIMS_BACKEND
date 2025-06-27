package com.app.LIMS.master.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.LIMS.master.dto.LabRegistrationRequest;
import com.app.LIMS.master.dto.LabRegistrationResponse;
import com.app.LIMS.master.entity.Lab;
import com.app.LIMS.master.service.LabService;

@RestController
@RequestMapping("/api/lab")
public class LabController {
    @Autowired
    private LabService labService;
    @PostMapping(value = "/register")
    public ResponseEntity<?> registerLab(@RequestBody LabRegistrationRequest request) {
        try {
            LabRegistrationResponse userdtl = labService.registerLab(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(userdtl);
        } catch (RuntimeException ex) {
            // For example, lab code already exists or validation fails
            // Return 409 Conflict with error message
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            // Generic error - 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getLab(@PathVariable("id") String id) {
        try {
            Lab lab = labService.getLabById(Long.parseLong(id));
            if (lab == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(Map.of("error", "Lab not found"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(lab);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "An unexpected error occurred."));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLab(@PathVariable("id") String id, @RequestBody LabRegistrationRequest request) {
        try {
            LabRegistrationResponse updatedLab = labService.updateLab( Long.parseLong(id), request);
            if (updatedLab == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(Map.of("error", "Lab not found"));
            }
            return ResponseEntity.status(HttpStatus.OK).body(updatedLab);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "An unexpected error occurred."));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLab(@PathVariable("id") Long id) {
        try {
            boolean isDeleted = labService.deleteLab(id);
            if (!isDeleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(Map.of("error", "Lab not found"));
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "An unexpected error occurred."));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllLabs() {
        try {
            List<Map<String, String>> labs = labService.getAllLabs();
            return ResponseEntity.status(HttpStatus.OK).body(labs);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "An unexpected error occurred."));
        }
    }

}