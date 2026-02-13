package com.riyobox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class R2StorageService {
    
    @Value("${r2.account-id}")
    private String accountId;
    
    @Value("${r2.access-key}")
    private String accessKey;
    
    @Value("${r2.secret-key}")
    private String secretKey;
    
    @Value("${r2.bucket-name}")
    private String bucketName;
    
    @Value("${r2.public-url}")
    private String publicUrl;
    
    public Map<String, String> generatePresignedUrl(String fileName, String contentType) {
        S3Presigner presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .region(Region.US_EAST_1)
                .endpointOverride(java.net.URI.create(
                        String.format("https://%s.r2.cloudflarestorage.com", accountId)
                ))
                .build();
        
        String key = "uploads/" + UUID.randomUUID() + "/" + fileName;
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();
        
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(putObjectRequest)
                .build();
        
        URL url = presigner.presignPutObject(presignRequest).url();
        String publicFileUrl = publicUrl + "/" + key;
        
        return Map.of("uploadUrl", url.toString(), "publicUrl", publicFileUrl);
    }
    
    public String getPublicUrl(String key) {
        return publicUrl + "/" + key;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public String getAccountId() { return accountId; }
    public String getBucketName() { return bucketName; }

    public String uploadFile(org.springframework.web.multipart.MultipartFile file, String folder) {
        // Mock implementation
        return publicUrl + "/" + folder + "/" + file.getOriginalFilename();
    }
}
