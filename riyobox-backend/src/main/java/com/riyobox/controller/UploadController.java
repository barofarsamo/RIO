package com.riyobox.controller;

import com.riyobox.service.R2StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {
    
    private final R2StorageService r2StorageService;
    
    @GetMapping("/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> getR2Config() {
        Map<String, String> config = new HashMap<>();
        config.put("accountId", r2StorageService.getAccountId());
        config.put("bucketName", r2StorageService.getBucketName());
        config.put("publicUrl", r2StorageService.getPublicUrl(""));
        return ResponseEntity.ok(config);
    }
    
    @PostMapping("/presigned")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> generatePresignedUrl(
            @RequestBody Map<String, String> request) {
        String fileName = request.get("fileName");
        String fileType = request.get("fileType");
        
        Map<String, String> urls = r2StorageService.generatePresignedUrl(fileName, fileType);
        return ResponseEntity.ok(urls);
    }
}
