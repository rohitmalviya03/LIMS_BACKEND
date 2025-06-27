package com.app.LIMS.master.Controller;

import com.app.LIMS.Dto.TestWithParamsDTO;
import com.app.LIMS.master.Repository.*;
import com.app.LIMS.master.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/masters")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MasterController {

    @Autowired private TestMasterRepository testRepo;
    @Autowired private SampleMasterRepository sampleRepo;
    @Autowired private MachineMasterRepository machineRepo;
    @Autowired private MachineParameterTestMasterRepository paramRepo;

    // --- Test Master ---
    @GetMapping("/tests")
    public List<TestMaster> getTests(@RequestParam(required = false) String labcode) {
        if (labcode != null && !labcode.isEmpty()) {
            return testRepo.findAllByLabcode(labcode);
        }
        return testRepo.findAll();
    }

    @GetMapping("/tests/{id}")
    public Optional<TestMaster> getTests(@PathVariable Long id) { return testRepo.findById(id); }

    @PostMapping("/tests")
    public ResponseEntity<Map<String, Object>> addTest(@RequestBody TestMaster test) {
        Map<String, Object> response = new HashMap<>();

      
try {
        TestMaster saved = testRepo.save(test);
        response.put("success", true);
        response.put("message", "Test added successfully.");
        response.put("data", saved);
        return ResponseEntity.ok(response);
        
    } catch (DataIntegrityViolationException e) {
        response.put("success", false);
        response.put("message", "A sample with the same labcode and type already exists.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    } catch (Exception e) {
        response.put("success", false);
        response.put("message", "Something went wrong.");
        return ResponseEntity.status(500).body(response);
    }
    }

    @PutMapping("/tests/{id}")
    public TestMaster updateTest(@PathVariable Long id, @RequestBody TestMaster test) {
        test.setId(id);
        return testRepo.save(test);
    }

    @DeleteMapping("/tests/{id}")
    public void deleteTest(@PathVariable Long id) { testRepo.deleteById(id); }

    // --- Sample Master ---
    @GetMapping("/samples")
    public List<SampleMaster> getSamples(@RequestParam(required = false) String labcode) {
        if (labcode != null && !labcode.isEmpty()) {
            return sampleRepo.findAllByLabcode(labcode);
        }
        return sampleRepo.findAll();
    }
    @PostMapping("/samples")
    public ResponseEntity<Map<String, Object>> addSample(@RequestBody SampleMaster sample) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = sampleRepo.existsByTypeAndLabcode( sample.getType(),sample.getLabcode());
        
        if (exists) {
            response.put("success", false);
            response.put("message", "Sample Type for this Lab already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        try {
            SampleMaster savedSample = sampleRepo.save(sample);
            response.put("success", true);
            response.put("message", "Sample added successfully.");
            response.put("data", savedSample);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            response.put("success", false);
            response.put("message", "A sample with the same labcode and type already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Something went wrong.");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping("/samples/{id}")
    public SampleMaster updateSample(@PathVariable Long id, @RequestBody SampleMaster sample) {
        sample.setId(id);
        return sampleRepo.save(sample);
    }

    @DeleteMapping("/samples/{id}")
    public void deleteSample(@PathVariable Long id) { sampleRepo.deleteById(id); }

    // --- Machine Master ---
    @GetMapping("/machines")
    public List<MachineMaster> getMachines(@RequestParam(required = false) String labcode) {
        if (labcode != null && !labcode.isEmpty()) {
            return machineRepo.findAllByLabcode(labcode);
        }
        return machineRepo.findAll();
    }

    @PostMapping("/machines")
    public ResponseEntity<Map<String, Object>>  addMachine(@RequestBody MachineMaster machine) {
    	   Map<String, Object> response = new HashMap<>();
    	  boolean exists = machineRepo.existsByNameAndLabcode(machine.getName(),machine.getLabcode());
    	   if (exists) {
               response.put("success", false);
               response.put("message", "Sample Type for this Lab already exists.");
               return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
           }
    	   machineRepo.save(machine); 
    	   response.put("success", true);
           response.put("message", "Sample added successfully.");
           response.put("data", machine);
           return ResponseEntity.ok(response);
    	
    
    
    }

    @PutMapping("/machines/{id}")
    public MachineMaster updateMachine(@PathVariable Long id, @RequestBody MachineMaster machine) {
        machine.setId(id);
        return machineRepo.save(machine);
    }

    @DeleteMapping("/machines/{id}")
    public void deleteMachine(@PathVariable Long id) { machineRepo.deleteById(id); }

    // --- Machine Parameter Test Master ---
    @GetMapping("/machine-params")
    public List<MachineParameterTestMaster> getParams(@RequestParam(required = false) String labcode) {
        if (labcode != null && !labcode.isEmpty()) {
            return paramRepo.findAllByLabcode(labcode);
        }
        return paramRepo.findAll();
    }

    @GetMapping("/machine-test-params")
    public List<MachineParameterTestMaster> getParamsByTestId(@RequestParam(required = false) Long testId) {
        if (testId != null) {
            return paramRepo.findByTestId(testId);
        } else {
            return paramRepo.findAll();
        }
    }

    @PostMapping("/machine-params")
    public ResponseEntity<Map<String, Object>>  addParam(@RequestBody MachineParameterTestMaster param) { 
    	 Map<String, Object> response = new HashMap<>();
    	 boolean exists = paramRepo.existsByParameterAndLabcodeAndTest(param.getParameter(),param.getLabcode(),param.getTest());
  	   if (exists) {
             response.put("success", false);
             response.put("message", "Sample Type for this Lab already exists.");
             return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
         }
    	 paramRepo.save(param); 
    	  response.put("success", true);
          response.put("message", "Sample added successfully.");
          response.put("data", param);
          return ResponseEntity.ok(response);
    
    }

    @PutMapping("/machine-params/{id}")
    public MachineParameterTestMaster updateParam(@PathVariable Long id, @RequestBody MachineParameterTestMaster param) {
        param.setId(id);
        return paramRepo.save(param);
    }

    @DeleteMapping("/machine-params/{id}")
    public void deleteParam(@PathVariable Long id) { paramRepo.deleteById(id); }

    @GetMapping("/tests-with-params")
    public List<TestWithParamsDTO> getTestsWithParams(@RequestParam(required = false) String labcode) {
        List<TestMaster> tests = (labcode != null && !labcode.isEmpty())
            ? testRepo.findAllByLabcode(labcode)
            : testRepo.findAll();
        List<MachineParameterTestMaster> allParams = (labcode != null && !labcode.isEmpty())
            ? paramRepo.findAllByLabcode(labcode)
            : paramRepo.findAll();

        return tests.stream().map(test -> {
            List<MachineParameterTestMaster> paramsForTest = allParams.stream()
                .filter(param -> param.getTest().getId().equals(test.getId()))
                .collect(Collectors.toList());
            return new TestWithParamsDTO(test.getId(), test.getTestName(), paramsForTest);
        }).collect(Collectors.toList());
    }
}