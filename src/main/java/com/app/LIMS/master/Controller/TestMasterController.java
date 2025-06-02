package com.app.LIMS.master.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.LIMS.master.Repository.TestMasterRepository;
import com.app.LIMS.master.entity.TestMaster;

@RestController
@RequestMapping("/api/tests-master")
public class TestMasterController {
    @Autowired private TestMasterRepository testMasterRepo;

    @GetMapping
    public List<TestMaster> getAllTests() {
        return testMasterRepo.findAll();
    }
    
    @PostMapping
    public ResponseEntity<?> addTest(@RequestBody TestMaster req) {
        // Optionally check for duplicate test name
        if (testMasterRepo.findByTestNameIgnoreCase(req.getTestName()).isPresent()) {
            return ResponseEntity.badRequest().body("Test already exists.");
        }
        TestMaster test = new TestMaster();
        test.setTestName(req.getTestName());
        test.setDescription(req.getDescription());
        testMasterRepo.save(test);
        return ResponseEntity.ok(test);
    }
}