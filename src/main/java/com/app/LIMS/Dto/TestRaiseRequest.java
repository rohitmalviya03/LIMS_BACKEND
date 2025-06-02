package com.app.LIMS.Dto;

public class TestRaiseRequest {
    private Long patientId;
    private Long testRaisedBy;
    private String testName;
    private String notes;
    // getters/setters
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