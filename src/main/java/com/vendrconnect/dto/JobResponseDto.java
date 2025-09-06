package com.vendrconnect.dto;

import java.time.LocalDateTime;

public class JobResponseDto {
    private Long id;
    private String jobTitle;
    private String description;
    private String serviceCategory;
    private String location;
    private Double budgetMin;
    private Double budgetMax;
    private String status;
    private String userId;
    private String assignedVendor;
    private String urgency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Customer and Vendor details
    private UserDto customer;
    private VendorDto assignedVendorDetails;
    
    public static class UserDto {
        private Long id;
        private String name;
        private String email;
        private String location;
        
        public UserDto(Long id, String name, String email, String location) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.location = location;
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
    }
    
    public static class VendorDto {
        private Long id;
        private String name;
        private String email;
        private String location;
        
        public VendorDto(Long id, String name, String email, String location) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.location = location;
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
    }
    
    // Constructors
    public JobResponseDto() {}
    
    // Getters and setters
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
    
    public UserDto getCustomer() { return customer; }
    public void setCustomer(UserDto customer) { this.customer = customer; }
    
    public VendorDto getAssignedVendorDetails() { return assignedVendorDetails; }
    public void setAssignedVendorDetails(VendorDto assignedVendorDetails) { this.assignedVendorDetails = assignedVendorDetails; }
}