package com.app.LIMS.master.entity;

import jakarta.persistence.*;

@Entity
public class MachineMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	private String model;
    @Column(nullable = true)
    private String labcode; // <-- Added labcode field


    public String getLabcode() {
		return labcode;
	}

	public void setLabcode(String labcode) {
		this.labcode = labcode;
	}
    private String manufacturer;

    // getters and setters
}