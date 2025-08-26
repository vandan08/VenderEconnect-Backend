package com.vendrconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vendrconnect.service.AuthService;
import com.vendrconnect.model.User;
import com.vendrconnect.model.Vendor;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProfileController {

    @Autowired
    private AuthService authService;

    @GetMapping("/user")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            String userId = authService.getUserIdFromToken(token.substring(7));
            User user = authService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/vendor")
    public ResponseEntity<?> getVendorProfile(@RequestHeader("Authorization") String token) {
        try {
            String vendorId = authService.getUserIdFromToken(token.substring(7));
            Vendor vendor = authService.getVendorById(vendorId);
            return ResponseEntity.ok(vendor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUserProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody User updatedUser) {
        try {
            String userId = authService.getUserIdFromToken(token.substring(7));
            User user = authService.updateUserProfile(userId, updatedUser);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/vendor")
    public ResponseEntity<?> updateVendorProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody Vendor updatedVendor) {
        try {
            String vendorId = authService.getUserIdFromToken(token.substring(7));
            Vendor vendor = authService.updateVendorProfile(vendorId, updatedVendor);
            return ResponseEntity.ok(vendor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> passwordData) {
        try {
            String userId = authService.getUserIdFromToken(token.substring(7));
            String userType = authService.getUserTypeFromToken(token.substring(7));
            String oldPassword = passwordData.get("oldPassword");
            String newPassword = passwordData.get("newPassword");
            
            boolean success = authService.changePassword(userId, userType, oldPassword, newPassword);
            if (success) {
                return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid old password"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}