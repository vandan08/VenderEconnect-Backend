package com.vendrconnect.repository;

import com.vendrconnect.model.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends MongoRepository<Vendor, String> {
    Optional<Vendor> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Vendor> findByServiceCategoryAndLocation(String serviceCategory, String location);
    List<Vendor> findByServiceCategory(String serviceCategory);
}