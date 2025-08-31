package com.vendrconnect.repository;

import com.vendrconnect.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByUserId(String userId);
    List<Job> findByAssignedVendor(String vendorId);
    List<Job> findByStatus(String status);
    List<Job> findByServiceCategory(String serviceCategory);
    
    @Query("SELECT j FROM Job j WHERE j.serviceCategory = :serviceCategory AND j.status = 'pending'")
    List<Job> findAvailableJobsByCategory(@Param("serviceCategory") String serviceCategory);
    
    @Query("SELECT j FROM Job j WHERE j.serviceCategory = :serviceCategory AND j.location = :location AND j.status = 'pending'")
    List<Job> findAvailableJobsByCategoryAndLocation(@Param("serviceCategory") String serviceCategory, @Param("location") String location);
}