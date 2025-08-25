package com.vendrconnect.repository;

import com.vendrconnect.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    List<Job> findByPostedBy(String userId);
    List<Job> findByAssignedVendor(String vendorId);
    List<Job> findByServiceCategoryAndLocationAndStatus(String serviceCategory, String location, String status);
    List<Job> findByServiceCategoryAndStatus(String serviceCategory, String status);
    List<Job> findByStatus(String status);
}