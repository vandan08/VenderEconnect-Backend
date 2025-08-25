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
        return vendorRepository.findById(vendorId).orElseThrow();
    }
    
    public Vendor updateAvailabilityStatus(String vendorId, String status) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow();
        vendor.setAvailabilityStatus(status);
        return vendorRepository.save(vendor);
    }
    
    public Vendor addTeamMember(String vendorId, TeamMember teamMember) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow();
        vendor.getTeamMembers().add(teamMember);
        return vendorRepository.save(vendor);
    }
    
    public Vendor removeTeamMember(String vendorId, String memberName) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow();
        vendor.getTeamMembers().removeIf(member -> member.getName().equals(memberName));
        return vendorRepository.save(vendor);
    }
    
    public Vendor updateTeamMember(String vendorId, String memberName, TeamMember updatedMember) {
        Vendor vendor = vendorRepository.findById(vendorId).orElseThrow();
        vendor.getTeamMembers().stream()
            .filter(member -> member.getName().equals(memberName))
            .findFirst()
            .ifPresent(member -> {
                member.setRole(updatedMember.getRole());
                member.setStatus(updatedMember.getStatus());
            });
        return vendorRepository.save(vendor);
    }
}