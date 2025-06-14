package com.app.LIMS.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;

@Entity
public class Result {
    @Id @GeneratedValue
    private Long id;
    private String enteredBy; // Add this field

    public String getEnteredBy() { return enteredBy; }
    public void setEnteredBy(String enteredBy) { this.enteredBy = enteredBy; }
    private String validationStatus; // e.g., "pending", "approved", "rejected"
    private String validatedBy;      // doctor userId
    private java.time.LocalDateTime validatedAt;
    private java.time.LocalDateTime enterdAt;
    public java.time.LocalDateTime getEnterdAt() {
		return enterdAt;
	}
	public void setEnterdAt(java.time.LocalDateTime enterdAt) {
		this.enterdAt = enterdAt;
	}
	public String getValidationStatus() { return validationStatus; }
    public void setValidationStatus(String validationStatus) { this.validationStatus = validationStatus; }
    public String getValidatedBy() { return validatedBy; }
    public void setValidatedBy(String validatedBy) { this.validatedBy = validatedBy; }
    public java.time.LocalDateTime getValidatedAt() { return validatedAt; }
    public void setValidatedAt(java.time.LocalDateTime validatedAt) { this.validatedAt = validatedAt; }
    @ManyToOne
    private Sample sample;

    @ManyToOne
    private TestSample test;

    private String value;

    // Add this field to support parameterized results
    private String parameter;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Sample getSample() {
        return sample;
    }
    public void setSample(Sample sample) {
        this.sample = sample;
    }
    public TestSample getTest() {
        return test;
    }
    public void setTest(TestSample test) {
        this.test = test;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getParameter() {
        return parameter;
    }
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}