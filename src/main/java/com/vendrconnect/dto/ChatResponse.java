package com.vendrconnect.dto;

import java.time.LocalDateTime;

public class ChatResponse {
    private String message;
    private String response;
    private LocalDateTime timestamp;
    private String userId;

    public ChatResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatResponse(String message, String response, String userId) {
        this.message = message;
        this.response = response;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}