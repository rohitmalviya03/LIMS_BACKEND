package com.app.LIMS.master.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.LIMS.master.Repository.TestMasterRepository;
import com.app.LIMS.master.entity.TestMaster;
@RestController
@RequestMapping("/api/tests-master")
public class TestMasterController {

    @Autowired
    private TestMasterRepository testMasterRepository;

    // Get all test masters
    @GetMapping
    public List<TestMaster> getAllTests() {
        return testMasterRepository.findAll();
    }

    // Add new test to master table
    @PostMapping
    public ResponseEntity<?> addTest(@RequestBody TestMaster testMaster) {
        // Optionally prevent duplicate test names
        if (testMasterRepository.findByTestNameIgnoreCase(testMaster.getTestName()).isPresent()) {
            return ResponseEntity.badRequest().body("Test already exists.");
        }
        TestMaster saved = testMasterRepository.save(testMaster);
        return ResponseEntity.ok(saved);
    }

    // (Optional) Get test master by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTestById(@PathVariable Long id) {
        return testMasterRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // (Optional) Update test master by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTest(@PathVariable Long id, @RequestBody TestMaster updated) {
        return testMasterRepository.findById(id)
                .map(test -> {
                    test.setTestName(updated.getTestName());
                    test.setDescription(updated.getDescription());
                    return ResponseEntity.ok(testMasterRepository.save(test));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // (Optional) Delete test master by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable Long id) {
        if (!testMasterRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        testMasterRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}