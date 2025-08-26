package com.vendrconnect.config;

import com.vendrconnect.model.Admin;
import com.vendrconnect.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        if (!adminRepository.existsByEmail("admin@vendrconnect.com")) {
            Admin admin = new Admin("Admin", "admin@vendrconnect.com", 
                                  passwordEncoder.encode("admin123"));
            adminRepository.save(admin);
        }
    }
}