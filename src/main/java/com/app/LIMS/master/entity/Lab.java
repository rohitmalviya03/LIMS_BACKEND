package com.app.LIMS.master.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "lab")
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lab_code", unique = true, nullable = false)
    private String labCode;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column
    private String location;

    @Column
    private String logo; // You can use String for image URL or base64 data, or use @Lob for binary

    public Lab(Long id, String labCode, String name, String pincode, String userid) {
		super();
		this.id = id;
		this.labCode = labCode;
		this.name = name;
		this.pincode = pincode;
		this.userid = userid;
	}
	public Lab() {
		// TODO Auto-generated constructor stub
	}
	@Column
    private String mode; // e.g. "online", "offline", etc.

    @Column
    private String status; // e.g. "active", "inactive", "pending"
 // ...existing code...
    @Column(nullable = false)
    private String pincode;

    private String userid;
    public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	private String password;
    
    
    
    
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    // ...existing code...
    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLabCode() { return labCode; }
    public void setLabCode(String labCode) { this.labCode = labCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}