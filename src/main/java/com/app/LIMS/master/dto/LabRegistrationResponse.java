package com.app.LIMS.master.dto;

public class LabRegistrationResponse {
    private String userid;
    private String password;

    public LabRegistrationResponse() {}  // Default constructor needed for Jackson

    public LabRegistrationResponse(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    // Getters and setters
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
}
