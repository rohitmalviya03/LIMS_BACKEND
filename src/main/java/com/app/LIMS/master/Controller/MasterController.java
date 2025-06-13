package com.app.LIMS.master.Controller;

import com.app.LIMS.Dto.TestWithParamsDTO;
import com.app.LIMS.master.Repository.*;
import com.app.LIMS.master.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    public List<TestMaster> getTests() { return testRepo.findAll(); }

    
    @GetMapping("/tests/{id}")
    public Optional<TestMaster> getTests(@PathVariable Long id) { return testRepo.findById(id); }

    @PostMapping("/tests")
    public TestMaster addTest(@RequestBody TestMaster test) { return testRepo.save(test); }

    @PutMapping("/tests/{id}")
    public TestMaster updateTest(@PathVariable Long id, @RequestBody TestMaster test) {
        test.setId(id);
        
        return testRepo.save(test);
    }

    @DeleteMapping("/tests/{id}")
    public void deleteTest(@PathVariable Long id) { testRepo.deleteById(id); }

    // --- Sample Master ---
    @GetMapping("/samples")
    public List<SampleMaster> getSamples() { return sampleRepo.findAll(); }

    @PostMapping("/samples")
    public SampleMaster addSample(@RequestBody SampleMaster sample) { return sampleRepo.save(sample); }

    @PutMapping("/samples/{id}")
    public SampleMaster updateSample(@PathVariable Long id, @RequestBody SampleMaster sample) {
        sample.setId(id);
        return sampleRepo.save(sample);
    }

    @DeleteMapping("/samples/{id}")
    public void deleteSample(@PathVariable Long id) { sampleRepo.deleteById(id); }

    // --- Machine Master ---
    @GetMapping("/machines")
    public List<MachineMaster> getMachines() { return machineRepo.findAll(); }

    @PostMapping("/machines")
    public MachineMaster addMachine(@RequestBody MachineMaster machine) { return machineRepo.save(machine); }

    @PutMapping("/machines/{id}")
    public MachineMaster updateMachine(@PathVariable Long id, @RequestBody MachineMaster machine) {
        machine.setId(id);
        return machineRepo.save(machine);
    }

    @DeleteMapping("/machines/{id}")
    public void deleteMachine(@PathVariable Long id) { machineRepo.deleteById(id); }

    // --- Machine Parameter Test Master ---
    @GetMapping("/machine-params")
    public List<MachineParameterTestMaster> getParams() { return paramRepo.findAll(); }

    @GetMapping("/machine-test-params")
    public List<MachineParameterTestMaster> getParamsByTestId(@RequestParam(required = false) Long testId) {
        if (testId != null) {
            return paramRepo.findByTestId(testId);
        } else {
            return paramRepo.findAll();
        }
    }
    @PostMapping("/machine-params")
    public MachineParameterTestMaster addParam(@RequestBody MachineParameterTestMaster param) { return paramRepo.save(param); }

    @PutMapping("/machine-params/{id}")
    public MachineParameterTestMaster updateParam(@PathVariable Long id, @RequestBody MachineParameterTestMaster param) {
        param.setId(id);
        return paramRepo.save(param);
    }

    @DeleteMapping("/machine-params/{id}")
    public void deleteParam(@PathVariable Long id) { paramRepo.deleteById(id); }

 
    @GetMapping("/tests-with-params")
    public List<TestWithParamsDTO> getTestsWithParams() {
        List<TestMaster> tests = testRepo.findAll();
        List<MachineParameterTestMaster> allParams = paramRepo.findAll();

        return tests.stream().map(test -> {
            List<MachineParameterTestMaster> paramsForTest = allParams.stream()
                .filter(param -> param.getId().equals(test.getId()))
                .collect(Collectors.toList());
            return new TestWithParamsDTO(test.getId(), test.getTestName(), paramsForTest);
        }).collect(Collectors.toList());
    }

}
