package com.riyobox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/stream")
@RequiredArgsConstructor
public class StreamController {
    
    private final S3Client s3Client;
    private final String bucketName = "riyobox-storage";
    
    @GetMapping("/video/{key}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InputStreamResource> streamVideo(
            @PathVariable String key,
            @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
        
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        
        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("video/mp4"));
        headers.set("Accept-Ranges", "bytes");
        headers.set("Content-Disposition", "inline");
        
        if (rangeHeader != null) {
            // Handle range requests for seeking
            String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
            long start = Long.parseLong(ranges[0]);
            Long end = ranges.length > 1 ? Long.parseLong(ranges[1]) : null;
            
            headers.set("Content-Range", "bytes " + start + "-" + 
                (end != null ? end : (response.response().contentLength() - 1)) + 
                "/" + response.response().contentLength());
            
            return new ResponseEntity<>(
                new InputStreamResource(response),
                headers,
                HttpStatus.PARTIAL_CONTENT
            );
        }
        
        return new ResponseEntity<>(
            new InputStreamResource(response),
            headers,
            HttpStatus.OK
        );
    }
}
