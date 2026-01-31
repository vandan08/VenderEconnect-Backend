package com.vendrconnect.service;

import com.vendrconnect.dto.JobRequest;
import com.vendrconnect.dto.JobResponseDto;
import com.vendrconnect.model.Job;
import com.vendrconnect.model.User;
import com.vendrconnect.model.Vendor;
import com.vendrconnect.repository.JobRepository;
import com.vendrconnect.repository.UserRepository;
import com.vendrconnect.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    public Job createJob(JobRequest request, String userId) {
        Job job = new Job(request.getJobTitle(), request.getDescription(),
                request.getServiceCategory(), request.getLocation(),
                request.getBudgetMin(), request.getBudgetMax(), userId);

        if (request.getUrgency() != null) {
            job.setUrgency(request.getUrgency());
        }

        job = jobRepository.save(job);

        // Add job to user's posted jobs list
        User user = userRepository.findById(Long.parseLong(userId)).orElseThrow();
        user.getJobsPosted().add(job.getId().toString());
        userRepository.save(user);

        return job;
    }

    public List<JobResponseDto> getUserJobs(String userId) {
        List<Job> jobs = jobRepository.findByUserId(userId);
        return jobs.stream().map(this::convertToJobResponseDto).collect(Collectors.toList());
    }

    public Job updateJob(String jobId, JobRequest request, String userId) {
        Job job = jobRepository.findById(Long.parseLong(jobId))
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));

        // Verify that the user owns this job
        if (!job.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You can only update your own jobs");
        }

        // Update job details
        job.setJobTitle(request.getJobTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setServiceCategory(request.getServiceCategory());
        job.setBudgetMin(request.getBudgetMin());
        job.setBudgetMax(request.getBudgetMax());
        if (request.getUrgency() != null) {
            job.setUrgency(request.getUrgency());
        }

        return jobRepository.save(job);
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

    public List<JobResponseDto> getAllJobsWithDetails() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream().map(this::convertToJobResponseDto).collect(Collectors.toList());
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    private JobResponseDto convertToJobResponseDto(Job job) {
        JobResponseDto dto = new JobResponseDto();
        dto.setId(job.getId());
        dto.setJobTitle(job.getJobTitle());
        dto.setDescription(job.getDescription());
        dto.setServiceCategory(job.getServiceCategory());
        dto.setLocation(job.getLocation());
        dto.setBudgetMin(job.getBudgetMin());
        dto.setBudgetMax(job.getBudgetMax());
        dto.setStatus(job.getStatus());
        dto.setUserId(job.getUserId());
        dto.setAssignedVendor(job.getAssignedVendor());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setUpdatedAt(job.getUpdatedAt());
        dto.setUrgency(job.getUrgency());

        // Get customer details
        if (job.getUserId() != null) {
            try {
                User customer = userRepository.findById(Long.parseLong(job.getUserId())).orElse(null);
                if (customer != null) {
                    dto.setCustomer(new JobResponseDto.UserDto(
                            customer.getId(),
                            customer.getName(),
                            customer.getEmail(),
                            customer.getLocation()));
                }
            } catch (Exception e) {
                // Handle parsing error
            }
        }

        // Get assigned vendor details
        if (job.getAssignedVendor() != null) {
            try {
                Vendor vendor = vendorRepository.findById(Long.parseLong(job.getAssignedVendor())).orElse(null);
                if (vendor != null) {
                    dto.setAssignedVendorDetails(new JobResponseDto.VendorDto(
                            vendor.getId(),
                            vendor.getName(),
                            vendor.getEmail(),
                            vendor.getLocation()));
                }
            } catch (Exception e) {
                // Handle parsing error
            }
        }

        return dto;
    }
}