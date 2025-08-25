package com.vendrconnect.controller;

import com.vendrconnect.dto.JobRequest;
import com.vendrconnect.model.Job;
import com.vendrconnect.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {
    
    @Autowired
    private JobService jobService;
    
    @PostMapping
    public ResponseEntity<Job> createJob(@Valid @RequestBody JobRequest request, Authentication auth) {
        String userId = auth.getName();
        Job job = jobService.createJob(request, userId);
        return ResponseEntity.ok(job);
    }
    
    @GetMapping("/user")
    public ResponseEntity<List<Job>> getUserJobs(Authentication auth) {
        String userId = auth.getName();
        List<Job> jobs = jobService.getUserJobs(userId);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/vendor")
    public ResponseEntity<List<Job>> getVendorJobs(Authentication auth) {
        String vendorId = auth.getName();
        List<Job> jobs = jobService.getVendorJobs(vendorId);
        return ResponseEntity.ok(jobs);
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<Job>> getAvailableJobs(@RequestParam String serviceCategory, 
                                                     @RequestParam(required = false) String location,
                                                     Authentication auth) {
        List<Job> jobs = jobService.getAvailableJobsForVendor(serviceCategory, location);
        return ResponseEntity.ok(jobs);
    }
    
    @PostMapping("/{jobId}/accept")
    public ResponseEntity<Job> acceptJob(@PathVariable String jobId, Authentication auth) {
        String vendorId = auth.getName();
        Job job = jobService.acceptJob(jobId, vendorId);
        return ResponseEntity.ok(job);
    }
    
    @PutMapping("/{jobId}/status")
    public ResponseEntity<Job> updateJobStatus(@PathVariable String jobId, 
                                              @RequestBody Map<String, String> request) {
        String status = request.get("status");
        Job job = jobService.updateJobStatus(jobId, status);
        return ResponseEntity.ok(job);
    }
    
    @GetMapping("/{jobId}")
    public ResponseEntity<Job> getJob(@PathVariable String jobId) {
        Job job = jobService.getJobById(jobId);
        return ResponseEntity.ok(job);
    }
}