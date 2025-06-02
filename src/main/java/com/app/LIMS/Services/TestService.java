package com.app.LIMS.Services;

import java.util.List;

import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.LIMS.Repository.TestSampleRepository;
import com.app.LIMS.entity.TestSample;
@Service
public class TestService {
	
    @Autowired
    private TestSampleRepository testRepository;

	public List<TestSample> getTestsByStatus(String status) {
        if (status == null || status.equalsIgnoreCase("All")) {
            return testRepository.findAll();
        } else {
            return testRepository.findByStatusIgnoreCase(status);
        }
    }
}
