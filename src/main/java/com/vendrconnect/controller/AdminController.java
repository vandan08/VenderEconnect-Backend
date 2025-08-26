package com.vendrconnect.controller;

import com.vendrconnect.model.User;
import com.vendrconnect.model.Vendor;
import com.vendrconnect.model.Job;
import com.vendrconnect.repository.UserRepository;
import com.vendrconnect.repository.VendorRepository;
import com.vendrconnect.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VendorRepository vendorRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Remove passwords for security
        users.forEach(user -> user.setPassword("***"));
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/vendors")
    public ResponseEntity<List<Vendor>> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        // Remove passwords for security
        vendors.forEach(vendor -> vendor.setPassword("***"));
        return ResponseEntity.ok(vendors);
    }
    
    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobRepository.findAll());
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Object> getStats() {
        return ResponseEntity.ok(new Object() {
            public final long totalUsers = userRepository.count();
            public final long totalVendors = vendorRepository.count();
            public final long totalJobs = jobRepository.count();
        });
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to delete user"));
        }
    }
    
    @DeleteMapping("/vendors/{id}")
    public ResponseEntity<Map<String, String>> deleteVendor(@PathVariable String id) {
        try {
            vendorRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Vendor deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to delete vendor"));
        }
    }
    
    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        List<User> users = userRepository.findAll().stream()
            .filter(user -> user.getName().toLowerCase().contains(query.toLowerCase()) ||
                           user.getEmail().toLowerCase().contains(query.toLowerCase()))
            .peek(user -> user.setPassword("***"))
            .toList();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/vendors/search")
    public ResponseEntity<List<Vendor>> searchVendors(@RequestParam String query) {
        List<Vendor> vendors = vendorRepository.findAll().stream()
            .filter(vendor -> vendor.getName().toLowerCase().contains(query.toLowerCase()) ||
                             vendor.getEmail().toLowerCase().contains(query.toLowerCase()))
            .peek(vendor -> vendor.setPassword("***"))
            .toList();
        return ResponseEntity.ok(vendors);
    }
}