package com.vendrconnect.service;

import com.vendrconnect.dto.*;
import com.vendrconnect.model.User;
import com.vendrconnect.model.Vendor;
import com.vendrconnect.repository.UserRepository;
import com.vendrconnect.repository.VendorRepository;
import com.vendrconnect.repository.AdminRepository;
import com.vendrconnect.model.Admin;
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
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    public AuthResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()) || 
            vendorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        String password = request.getPassword();
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("Password is required for registration");
        }
        
        if (password.length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters long");
        }
        
        User user = new User(request.getName(), request.getEmail(), 
                           passwordEncoder.encode(password), request.getLocation());
        user = userRepository.save(user);
        
        String token = jwtUtils.generateJwtToken(user.getId().toString(), user.getEmail(), "USER");
        return new AuthResponse(token, user.getId().toString(), user.getName(), user.getEmail(), "USER");
    }
    
    public AuthResponse registerVendor(VendorRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()) || 
            vendorRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        String password = request.getPassword();
        if (password == null || password.trim().isEmpty()) {
            throw new RuntimeException("Password is required for registration");
        }
        
        if (password.length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters long");
        }
        
        Vendor vendor = new Vendor();
        vendor.setName(request.getName());
        vendor.setEmail(request.getEmail());
        vendor.setPassword(passwordEncoder.encode(password));
        vendor.setLocation(request.getLocation());
        vendor.setServiceCategories(request.getServiceCategories());
        vendor = vendorRepository.save(vendor);
        
        String token = jwtUtils.generateJwtToken(vendor.getId().toString(), vendor.getEmail(), "VENDOR");
        return new AuthResponse(token, vendor.getId().toString(), vendor.getName(), vendor.getEmail(), "VENDOR");
    }
    
    public AuthResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);
        
        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtUtils.generateJwtToken(user.getId().toString(), user.getEmail(), "USER");
            return new AuthResponse(token, user.getId().toString(), user.getName(), user.getEmail(), "USER");
        }
        
        throw new RuntimeException("Invalid credentials");
    }
    
    public AuthResponse loginVendor(LoginRequest request) {
        Vendor vendor = vendorRepository.findByEmail(request.getEmail())
                .orElse(null);
        
        if (vendor != null && passwordEncoder.matches(request.getPassword(), vendor.getPassword())) {
            String token = jwtUtils.generateJwtToken(vendor.getId().toString(), vendor.getEmail(), "VENDOR");
            return new AuthResponse(token, vendor.getId().toString(), vendor.getName(), vendor.getEmail(), "VENDOR");
        }
        
        throw new RuntimeException("Invalid credentials");
    }
    
    public AuthResponse loginAdmin(LoginRequest request) {
        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElse(null);
        
        if (admin != null && passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            String token = jwtUtils.generateJwtToken(admin.getId().toString(), admin.getEmail(), "ADMIN");
            return new AuthResponse(token, admin.getId().toString(), admin.getName(), admin.getEmail(), "ADMIN");
        }
        
        throw new RuntimeException("Invalid credentials");
    }
    
    public String getUserIdFromToken(String token) {
        return jwtUtils.getUserIdFromJwtToken(token);
    }
    
    public String getUserTypeFromToken(String token) {
        return jwtUtils.getRoleFromJwtToken(token);
    }
    
    public User getUserById(String userId) {
        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public Vendor getVendorById(String vendorId) {
        return vendorRepository.findById(Long.parseLong(vendorId))
                .orElseThrow(() -> new RuntimeException("Vendor not found"));
    }
    
    public User updateUserProfile(String userId, User updatedUser) {
        User user = getUserById(userId);
        user.setName(updatedUser.getName());
        user.setLocation(updatedUser.getLocation());
        return userRepository.save(user);
    }
    
    public Vendor updateVendorProfile(String vendorId, Vendor updatedVendor) {
        Vendor vendor = getVendorById(vendorId);
        vendor.setName(updatedVendor.getName());
        vendor.setLocation(updatedVendor.getLocation());
        vendor.setServiceCategories(updatedVendor.getServiceCategories());
        return vendorRepository.save(vendor);
    }
    
    public void updateProfileImage(String userId, String userType, String imageUrl) {
        if ("USER".equals(userType)) {
            User user = getUserById(userId);
            user.setProfileImage(imageUrl);
            userRepository.save(user);
        } else if ("VENDOR".equals(userType)) {
            Vendor vendor = getVendorById(userId);
            vendor.setProfileImage(imageUrl);
            vendorRepository.save(vendor);
        }
    }
    
    public boolean changePassword(String userId, String userType, String oldPassword, String newPassword) {
        if ("USER".equals(userType)) {
            User user = getUserById(userId);
            if (passwordEncoder.matches(oldPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        } else if ("VENDOR".equals(userType)) {
            Vendor vendor = getVendorById(userId);
            if (passwordEncoder.matches(oldPassword, vendor.getPassword())) {
                vendor.setPassword(passwordEncoder.encode(newPassword));
                vendorRepository.save(vendor);
                return true;
            }
        }
        return false;
    }
}