package com.vendrconnect.repository;

import com.vendrconnect.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findByEmail(String email);
    boolean existsByEmail(String email);
    
    @Query("SELECT v FROM Vendor v JOIN v.serviceCategories sc WHERE sc = :serviceCategory")
    List<Vendor> findByServiceCategory(@Param("serviceCategory") String serviceCategory);
    
    @Query("SELECT v FROM Vendor v WHERE v.name LIKE %:query% OR v.email LIKE %:query% OR v.location LIKE %:query%")
    List<Vendor> searchVendors(@Param("query") String query);
}