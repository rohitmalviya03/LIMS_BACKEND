package com.app.LIMS.master.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "lab_user",
    uniqueConstraints = @UniqueConstraint(columnNames = {"email", "lab_id"})
)
public class LabUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isUserLock() {
		return userLock;
	}

	public void setUserLock(boolean userLock) {
		this.userLock = userLock;
	}

	public Lab getLab() {
		return lab;
	}

	public void setLab(Lab lab) {
		this.lab = lab;
	}

	@Column(nullable = false)
    private String password;

    @Column(name = "user_lock", nullable = false)
    private boolean userLock = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    // getters and setters
}