package com.vendrconnect.controller;

import com.vendrconnect.dto.*;
import com.vendrconnect.service.AuthService;
import com.vendrconnect.service.GoogleAuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private GoogleAuthService googleAuthService;

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            logger.info("User registration attempt for email: {}", request.getEmail());
            AuthResponse response = authService.registerUser(request);
            logger.info("User registration successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("User registration failed for email: {}", request.getEmail(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/register/vendor")
    public ResponseEntity<?> registerVendor(@Valid @RequestBody VendorRegistrationRequest request) {
        try {
            logger.info("Vendor registration attempt for email: {}", request.getEmail());
            AuthResponse response = authService.registerVendor(request);
            logger.info("Vendor registration successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Vendor registration failed for email: {}", request.getEmail(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login/user")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
        try {
            logger.info("User login attempt for email: {}", request.getEmail());
            AuthResponse response = authService.loginUser(request);
            logger.info("User login successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("User login failed for email: {}", request.getEmail(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login/vendor")
    public ResponseEntity<?> loginVendor(@Valid @RequestBody LoginRequest request) {
        try {
            logger.info("Vendor login attempt for email: {}", request.getEmail());
            AuthResponse response = authService.loginVendor(request);
            logger.info("Vendor login successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Vendor login failed for email: {}", request.getEmail(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginRequest request) {
        try {
            logger.info("Admin login attempt for email: {}", request.getEmail());
            AuthResponse response = authService.loginAdmin(request);
            logger.info("Admin login successful for email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Admin login failed for email: {}", request.getEmail(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleAuth(@RequestBody GoogleAuthRequest request) {
        try {
            logger.info("Google OAuth attempt for user type: {}", request.getUserType());
            AuthResponse response = googleAuthService.authenticateWithGoogle(request);
            logger.info("Google OAuth successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Google OAuth failed", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}