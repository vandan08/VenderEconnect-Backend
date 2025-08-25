package com.vendrconnect.controller;

import com.vendrconnect.model.TeamMember;
import com.vendrconnect.model.Vendor;
import com.vendrconnect.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
@CrossOrigin(origins = "*")
public class VendorController {
    
    @Autowired
    private VendorService vendorService;
    
    @GetMapping("/profile")
    public ResponseEntity<Vendor> getProfile(Authentication auth) {
        String vendorId = auth.getName();
        Vendor vendor = vendorService.getVendorById(vendorId);
        return ResponseEntity.ok(vendor);
    }
    
    @PutMapping("/availability")
    public ResponseEntity<Vendor> updateAvailability(@RequestBody Map<String, String> request, 
                                                     Authentication auth) {
        String vendorId = auth.getName();
        String status = request.get("status");
        Vendor vendor = vendorService.updateAvailabilityStatus(vendorId, status);
        return ResponseEntity.ok(vendor);
    }
    
    @PostMapping("/team")
    public ResponseEntity<Vendor> addTeamMember(@RequestBody TeamMember teamMember, 
                                               Authentication auth) {
        String vendorId = auth.getName();
        Vendor vendor = vendorService.addTeamMember(vendorId, teamMember);
        return ResponseEntity.ok(vendor);
    }
    
    @DeleteMapping("/team/{memberName}")
    public ResponseEntity<Vendor> removeTeamMember(@PathVariable String memberName, 
                                                  Authentication auth) {
        String vendorId = auth.getName();
        Vendor vendor = vendorService.removeTeamMember(vendorId, memberName);
        return ResponseEntity.ok(vendor);
    }
    
    @PutMapping("/team/{memberName}")
    public ResponseEntity<Vendor> updateTeamMember(@PathVariable String memberName, 
                                                  @RequestBody TeamMember teamMember,
                                                  Authentication auth) {
        String vendorId = auth.getName();
        Vendor vendor = vendorService.updateTeamMember(vendorId, memberName, teamMember);
        return ResponseEntity.ok(vendor);
    }
}