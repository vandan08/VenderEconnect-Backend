package com.vendrconnect.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;
    
    private String password;
    
    private String location;
    private String profileImage;
    
    @ElementCollection
    @CollectionTable(name = "vendor_service_categories", joinColumns = @JoinColumn(name = "vendor_id"))
    @Column(name = "service_category")
    private List<String> serviceCategories = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "vendor_team_members", joinColumns = @JoinColumn(name = "vendor_id"))
    @Column(name = "team_member")
    private List<String> teamMembers = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "vendor_jobs", joinColumns = @JoinColumn(name = "vendor_id"))
    @Column(name = "job_id")
    private List<String> jobsAccepted = new ArrayList<>();
    
    private boolean isAvailable = true;

    public Vendor() {}

    public Vendor(String name, String email, String password, String location, List<String> serviceCategories) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.serviceCategories = serviceCategories;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public List<String> getServiceCategories() { return serviceCategories; }
    public void setServiceCategories(List<String> serviceCategories) { this.serviceCategories = serviceCategories; }

    public List<String> getTeamMembers() { return teamMembers; }
    public void setTeamMembers(List<String> teamMembers) { this.teamMembers = teamMembers; }

    public List<String> getJobsAccepted() { return jobsAccepted; }
    public void setJobsAccepted(List<String> jobsAccepted) { this.jobsAccepted = jobsAccepted; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}