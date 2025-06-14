package com.app.LIMS.Dto;

import java.util.List;

import com.app.LIMS.master.entity.MachineParameterTestMaster;

public class TestWithParamsDTO {
    public Long id;
    public String testName;
    public List<MachineParameterTestMaster> parameters;

    public TestWithParamsDTO(Long id, String testName, List<MachineParameterTestMaster> parameters) {
        this.id = id;
        this.testName = testName;
        this.parameters = parameters;
    }
}