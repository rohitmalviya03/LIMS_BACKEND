package com.app.LIMS.entity;

import jakarta.persistence.Column;

public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String role;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
    @Column(nullable = true)
    private String labcode; // <-- Added labcode field


    public String getLabcode() {
		return labcode;
	}

	public void setLabcode(String labcode) {
		this.labcode = labcode;
	}

    // getters and setters
}