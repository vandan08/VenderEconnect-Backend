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
                         request.getLocation(), userId, request.getServiceCategory());
        if (request.getBudgetMin() != null) {
            job.setBudgetMin(request.getBudgetMin());
        }
        if (request.getBudgetMax() != null) {
            job.setBudgetMax(request.getBudgetMax());
        }
        job = jobRepository.save(job);
        
        // Add job to user's posted jobs list
        User user = userRepository.findById(userId).orElseThrow();
        user.getJobsPosted().add(job.getId());
        userRepository.save(user);
        
        return job;
    }
    
    public List<Job> getUserJobs(String userId) {
        return jobRepository.findByPostedBy(userId);
    }
    
    public List<Job> getVendorJobs(String vendorId) {
        return jobRepository.findByAssignedVendor(vendorId);
    }
    
    public List<Job> getAvailableJobsForVendor(String serviceCategory, String location) {
        List<Job> jobs = jobRepository.findByServiceCategoryAndLocationAndStatus(serviceCategory, location, "pending");
        if (jobs.isEmpty()) {
            jobs = jobRepository.findByServiceCategoryAndStatus(serviceCategory, "pending");
        }
        return jobs;
    }
    
    public Job acceptJob(String jobId, String vendorId) {
        Job job = jobRepository.findById(jobId).orElseThrow();
        job.setStatus("accepted");
        job.setAssignedVendor(vendorId);
        return jobRepository.save(job);
    }
    
    public Job updateJobStatus(String jobId, String status) {
        Job job = jobRepository.findById(jobId).orElseThrow();
        job.setStatus(status);
        return jobRepository.save(job);
    }
    
    public Job getJobById(String jobId) {
        return jobRepository.findById(jobId).orElseThrow();
    }
}