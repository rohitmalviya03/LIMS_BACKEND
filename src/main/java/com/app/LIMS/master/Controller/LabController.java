package com.app.LIMS.master.Controller;

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
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
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

}