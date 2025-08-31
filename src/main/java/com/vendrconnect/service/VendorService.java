package com.vendrconnect.service;

import com.vendrconnect.model.TeamMember;
import com.vendrconnect.model.Vendor;
import com.vendrconnect.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorService {
    
    @Autowired
    private VendorRepository vendorRepository;
    
    public Vendor getVendorById(String vendorId) {
        return vendorRepository.findById(Long.parseLong(vendorId)).orElseThrow();
    }
    
    public Vendor updateAvailabilityStatus(String vendorId, boolean isAvailable) {
        Vendor vendor = vendorRepository.findById(Long.parseLong(vendorId)).orElseThrow();
        vendor.setAvailable(isAvailable);
        return vendorRepository.save(vendor);
    }
    
    public Vendor addTeamMember(String vendorId, String memberName) {
        Vendor vendor = vendorRepository.findById(Long.parseLong(vendorId)).orElseThrow();
        vendor.getTeamMembers().add(memberName);
        return vendorRepository.save(vendor);
    }
    
    public Vendor removeTeamMember(String vendorId, String memberName) {
        Vendor vendor = vendorRepository.findById(Long.parseLong(vendorId)).orElseThrow();
        vendor.getTeamMembers().remove(memberName);
        return vendorRepository.save(vendor);
    }
}