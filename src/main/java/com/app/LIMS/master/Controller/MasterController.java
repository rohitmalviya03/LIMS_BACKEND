package com.app.LIMS.master.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.LIMS.master.Repository.*;
import com.app.LIMS.master.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/masters")
public class MasterController {

    @Autowired private TestMasterRepository testRepo;
    @Autowired private SampleMasterRepository sampleRepo;
    @Autowired private MachineMasterRepository machineRepo;
    @Autowired private MachineParameterTestMasterRepository paramRepo;

    // --- Test Master ---
    @GetMapping("/tests")
    public List<TestMaster> getTests() { return testRepo.findAll(); }

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

    @PostMapping("/machine-params")
    public MachineParameterTestMaster addParam(@RequestBody MachineParameterTestMaster param) { return paramRepo.save(param); }

    @PutMapping("/machine-params/{id}")
    public MachineParameterTestMaster updateParam(@PathVariable Long id, @RequestBody MachineParameterTestMaster param) {
        param.setId(id);
        return paramRepo.save(param);
    }

    @DeleteMapping("/machine-params/{id}")
    public void deleteParam(@PathVariable Long id) { paramRepo.deleteById(id); }
}

