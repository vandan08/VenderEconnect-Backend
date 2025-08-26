package com.vendrconnect.controller;

import com.vendrconnect.model.User;
import com.vendrconnect.model.Vendor;
import com.vendrconnect.model.Job;
import com.vendrconnect.repository.UserRepository;
import com.vendrconnect.repository.VendorRepository;
import com.vendrconnect.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}