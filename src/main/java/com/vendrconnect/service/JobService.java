package com.vendrconnect.service;

import com.vendrconnect.dto.JobRequest;
import com.vendrconnect.model.Job;
import com.vendrconnect.model.User;
import com.vendrconnect.repository.JobRepository;
import com.vendrconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Job createJob(JobRequest request, String userId) {
        Job job = new Job(request.getJobTitle(), request.getDescription(), 
                         request.getServiceCategory(), request.getLocation(), 
                         request.getBudgetMin(), request.getBudgetMax(), userId);

        job = jobRepository.save(job);
        
        // Add job to user's posted jobs list
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow();
        user.getJobsPosted().add(job.getId().toString());
        userRepository.save(user);
        
        return job;
    }
    
    public List<Job> getUserJobs(String userId) {
        return jobRepository.findByUserId(userId);
    }
    
    public List<Job> getVendorJobs(String vendorId) {
        return jobRepository.findByAssignedVendor(vendorId);
    }
    
    public List<Job> getAvailableJobsForVendor(String serviceCategory, String location) {
        List<Job> jobs;
        if (location != null && !location.isEmpty()) {
            jobs = jobRepository.findAvailableJobsByCategoryAndLocation(serviceCategory, location);
        } else {
            jobs = jobRepository.findAvailableJobsByCategory(serviceCategory);
        }
        return jobs;
    }
    
    public Job acceptJob(String jobId, String vendorId) {
        Job job = jobRepository.findById(Long.parseLong(jobId))
            .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        job.setStatus("in_progress");
        job.setAssignedVendor(vendorId);
        return jobRepository.save(job);
    }
    
    public Job updateJobStatus(String jobId, String status) {
        Job job = jobRepository.findById(Long.parseLong(jobId))
            .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        job.setStatus(status);
        return jobRepository.save(job);
    }
    
    public Job getJobById(String jobId) {
        return jobRepository.findById(Long.parseLong(jobId))
            .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
    }
    
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
}