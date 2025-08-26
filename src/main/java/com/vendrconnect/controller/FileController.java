package com.vendrconnect.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.vendrconnect.service.FileService;
import com.vendrconnect.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private AuthService authService;

    @PostMapping("/upload-profile-image")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token) {
        try {
            String userId = authService.getUserIdFromToken(token.substring(7));
            String userType = authService.getUserTypeFromToken(token.substring(7));
            
            String imageUrl = fileService.saveProfileImage(file, userId);
            authService.updateProfileImage(userId, userType, imageUrl);
            
            return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}