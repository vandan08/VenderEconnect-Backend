package com.vendrconnect.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.vendrconnect.dto.AuthResponse;
import com.vendrconnect.dto.GoogleAuthRequest;
import com.vendrconnect.model.User;
import com.vendrconnect.model.Vendor;
import com.vendrconnect.repository.UserRepository;
import com.vendrconnect.repository.VendorRepository;
import com.vendrconnect.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Service
public class GoogleAuthService {

    @Value("${google.oauth.client-id}")
    private String googleClientId;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthResponse authenticateWithGoogle(GoogleAuthRequest request) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(request.getToken());
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            if ("USER".equals(request.getUserType())) {
                return handleUserAuth(email, name);
            } else if ("VENDOR".equals(request.getUserType())) {
                return handleVendorAuth(email, name, request.getServiceCategories());
            }
        }
        throw new RuntimeException("Invalid Google token");
    }

    private AuthResponse handleUserAuth(String email, String name) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(""); // No password for Google auth
            user.setLocation(""); // Can be updated later
            user = userRepository.save(user);
        }

        String token = jwtUtils.generateJwtToken(user.getId(), user.getEmail(), "USER");
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), "USER");
    }

    private AuthResponse handleVendorAuth(String email, String name, String[] serviceCategories) {
        Optional<Vendor> existingVendor = vendorRepository.findByEmail(email);
        Vendor vendor;

        if (existingVendor.isPresent()) {
            vendor = existingVendor.get();
        } else {
            vendor = new Vendor();
            vendor.setName(name);
            vendor.setEmail(email);
            vendor.setPassword(""); // No password for Google auth
            vendor.setLocation(""); // Can be updated later
            if (serviceCategories != null) {
                vendor.setServiceCategories(Arrays.asList(serviceCategories));
            }
            vendor = vendorRepository.save(vendor);
        }

        String token = jwtUtils.generateJwtToken(vendor.getId(), vendor.getEmail(), "VENDOR");
        return new AuthResponse(token, vendor.getId(), vendor.getName(), vendor.getEmail(), "VENDOR");
    }
}