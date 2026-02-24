package com.riyobox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class R2StorageService {
    
    @Value("${cloudflare.r2.account-id:}")
    private String accountId;
    
    @Value("${cloudflare.r2.access-key-id:}")
    private String accessKey;
    
    @Value("${cloudflare.r2.secret-access-key:}")
    private String secretKey;
    
    @Value("${cloudflare.r2.bucket-name:riyobox-storage}")
    private String bucketName;
    
    @Value("${cloudflare.r2.public-url:}")
    private String publicUrl;

    private S3Client getS3Client() {
        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .region(Region.US_EAST_1)
                .endpointOverride(java.net.URI.create(
                        String.format("https://%s.r2.cloudflarestorage.com", accountId)
                ))
                .build();
    }
    
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
        
        Map<String, String> result = new HashMap<>();
        result.put("uploadUrl", url.toString());
        result.put("publicUrl", publicFileUrl);
        return result;
    }

    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String key = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        getS3Client().putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return publicUrl + "/" + key;
    }
    
    public String getPublicUrl(String key) {
        return publicUrl + "/" + key;
    }

    public String getAccountId() { return accountId; }
    public String getBucketName() { return bucketName; }
    public String getPublicUrlBase() { return publicUrl; }
}
