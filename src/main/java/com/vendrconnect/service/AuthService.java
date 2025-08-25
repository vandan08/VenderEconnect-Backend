package com.vendrconnect.service;

import com.vendrconnect.dto.*;
import com.vendrconnect.model.User;
import com.vendrconnect.model.Vendor;
import com.vendrconnect.repository.UserRepository;
import com.vendrconnect.repository.VendorRepository;
import com.vendrconnect.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VendorRepository vendorRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    public AuthResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()) || 
            vendorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User(request.getName(), request.getEmail(), 
                           passwordEncoder.encode(request.getPassword()), request.getLocation());
        user = userRepository.save(user);
        
        String token = jwtUtils.generateJwtToken(user.getId(), user.getEmail(), "USER");
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), "USER");
    }
    
    public AuthResponse registerVendor(VendorRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()) || 
            vendorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        Vendor vendor = new Vendor(request.getName(), request.getEmail(), 
                                 passwordEncoder.encode(request.getPassword()), 
                                 request.getServiceCategory(), request.getLocation());
        vendor = vendorRepository.save(vendor);
        
        String token = jwtUtils.generateJwtToken(vendor.getId(), vendor.getEmail(), "VENDOR");
        return new AuthResponse(token, vendor.getId(), vendor.getName(), vendor.getEmail(), "VENDOR");
    }
    
    public AuthResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);
        
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtUtils.generateJwtToken(user.getId(), user.getEmail(), "USER");
            return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), "USER");
        }
        
        throw new RuntimeException("Invalid credentials");
    }
    
    public AuthResponse loginVendor(LoginRequest request) {
        Vendor vendor = vendorRepository.findByEmail(request.getEmail())
                .orElse(null);
        
        if (vendor != null && passwordEncoder.matches(request.getPassword(), vendor.getPassword())) {
            String token = jwtUtils.generateJwtToken(vendor.getId(), vendor.getEmail(), "VENDOR");
            return new AuthResponse(token, vendor.getId(), vendor.getName(), vendor.getEmail(), "VENDOR");
        }
        
        throw new RuntimeException("Invalid credentials");
    }
}