package com.vendrconnect.dto;

public class GoogleAuthRequest {
    private String token;
    private String userType; // "USER" or "VENDOR"
    private String[] serviceCategories; // Only for vendors

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }

    public String[] getServiceCategories() { return serviceCategories; }
    public void setServiceCategories(String[] serviceCategories) { this.serviceCategories = serviceCategories; }
}