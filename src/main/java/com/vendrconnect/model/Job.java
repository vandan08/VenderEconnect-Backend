package com.vendrconnect.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String jobTitle;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotBlank
    private String serviceCategory;
    
    private String location;
    private Double budgetMin;
    private Double budgetMax;
    private String status = "pending";
    private String urgency = "normal";
    private String userId;
    private String assignedVendor;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "urgency")
    private String urgency = "normal";

    public Job() {}

    public Job(String jobTitle, String description, String serviceCategory, String location, 
               Double budgetMin, Double budgetMax, String userId) {
        this.jobTitle = jobTitle;
        this.description = description;
        this.serviceCategory = serviceCategory;
        this.location = location;
        this.budgetMin = budgetMin;
        this.budgetMax = budgetMax;
        this.userId = userId;
        this.urgency = "normal";
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getServiceCategory() { return serviceCategory; }
    public void setServiceCategory(String serviceCategory) { this.serviceCategory = serviceCategory; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Double getBudgetMin() { return budgetMin; }
    public void setBudgetMin(Double budgetMin) { this.budgetMin = budgetMin; }

    public Double getBudgetMax() { return budgetMax; }
    public void setBudgetMax(Double budgetMax) { this.budgetMax = budgetMax; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAssignedVendor() { return assignedVendor; }
    public void setAssignedVendor(String assignedVendor) { this.assignedVendor = assignedVendor; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }
}