package com.app.LIMS.Dto;

import java.util.List;

public class TestRaiseRequest {
    private Long patientId;
    private Long testRaisedBy;
    private String testName;
    private String notes; 
    private List<TestItem> tests;
    // getters/setters
    
    public static class TestItem {
        private String testName;

        // Getters and setters
        public String getTestName() { return testName; }
        public void setTestName(String testName) { this.testName = testName; }
    }
    public List<TestItem> getTests() { return tests; }
    public void setTests(List<TestItem> tests) { this.tests = tests; }
 
	public Long getPatientId() {
		return patientId;
	}
	public Long getTestRaisedBy() {
		return testRaisedBy;
	}
	public void setTestRaisedBy(Long testRaisedBy) {
		this.testRaisedBy = testRaisedBy;
	}
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
}