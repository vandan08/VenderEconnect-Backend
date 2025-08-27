package com.vendrconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class VendorRegistrationRequest {
    @NotBlank
    private String name;
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String password;
    
    private List<String> serviceCategories;
    
    @NotBlank
    private String location;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getServiceCategories() { return serviceCategories; }
    public void setServiceCategories(List<String> serviceCategories) { this.serviceCategories = serviceCategories; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}