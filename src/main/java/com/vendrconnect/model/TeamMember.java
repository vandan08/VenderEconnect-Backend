package com.vendrconnect.model;

public class TeamMember {
    private String name;
    private String role;
    private String status;

    public TeamMember() {}

    public TeamMember(String name, String role, String status) {
        this.name = name;
        this.role = role;
        this.status = status;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}