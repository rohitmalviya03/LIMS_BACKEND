package com.app.LIMS.master.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "test_master")
public class TestMaster {
    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String testName;

    private String description;
    @ManyToOne
    @JoinColumn(name = "sample_id")
    private SampleMaster sample; // Reference to Sample Master


    public SampleMaster getSample() {
		return sample;
	}
    @Column(nullable = true)
    private String labcode; // <-- Added labcode field


    public String getLabcode() {
		return labcode;
	}

	public void setLabcode(String labcode) {
		this.labcode = labcode;
	}
	public void setSample(SampleMaster sample) {
		this.sample = sample;
	}
    @Column(nullable = false)
    private Double price; // <--- Add this field

    // getters/setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTestName() {
        return testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
}