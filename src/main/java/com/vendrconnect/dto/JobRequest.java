package com.vendrconnect.dto;

import jakarta.validation.constraints.NotBlank;

public class JobRequest {
    @NotBlank
    private String jobTitle;
    
    @NotBlank
    private String description;
    
    @NotBlank
    private String location;
    
    @NotBlank
    private String serviceCategory;

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getServiceCategory() { return serviceCategory; }
    public void setServiceCategory(String serviceCategory) { this.serviceCategory = serviceCategory; }
}