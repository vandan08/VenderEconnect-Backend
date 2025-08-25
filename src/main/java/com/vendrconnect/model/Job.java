package com.vendrconnect.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "jobs")
public class Job {
    @Id
    private String id;
    
    private String jobTitle;
    private String description;
    private String location;
    private String postedBy;
    private String status = "pending";
    private String assignedVendor;
    private String assignedTeamMember;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String serviceCategory;

    public Job() {}

    public Job(String jobTitle, String description, String location, String postedBy, String serviceCategory) {
        this.jobTitle = jobTitle;
        this.description = description;
        this.location = location;
        this.postedBy = postedBy;
        this.serviceCategory = serviceCategory;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPostedBy() { return postedBy; }
    public void setPostedBy(String postedBy) { this.postedBy = postedBy; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAssignedVendor() { return assignedVendor; }
    public void setAssignedVendor(String assignedVendor) { this.assignedVendor = assignedVendor; }

    public String getAssignedTeamMember() { return assignedTeamMember; }
    public void setAssignedTeamMember(String assignedTeamMember) { this.assignedTeamMember = assignedTeamMember; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getServiceCategory() { return serviceCategory; }
    public void setServiceCategory(String serviceCategory) { this.serviceCategory = serviceCategory; }
}