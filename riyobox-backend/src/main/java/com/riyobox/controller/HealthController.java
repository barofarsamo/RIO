package com.riyobox.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {
    
    private final MongoTemplate mongoTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        boolean mongoHealthy = false;
        boolean redisHealthy = false;
        String backendVersion = "1.0.0";
        
        try {
            // Check MongoDB
            mongoTemplate.executeCommand("{ ping: 1 }");
            mongoHealthy = true;
        } catch (Exception e) {
            mongoHealthy = false;
        }
        
        try {
            // Check Redis
            RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
            connection.ping();
            connection.close();
            redisHealthy = true;
        } catch (Exception e) {
            redisHealthy = false;
        }
        
        Map<String, Object> health = Map.of(
            "status", mongoHealthy && redisHealthy ? "UP" : "DOWN",
            "backend", Map.of(
                "version", backendVersion,
                "status", "UP"
            ),
            "mongodb", Map.of(
                "status", mongoHealthy ? "UP" : "DOWN",
                "database", "riyobox"
            ),
            "redis", Map.of(
                "status", redisHealthy ? "UP" : "DOWN",
                "usedMemory", redisTemplate.getConnectionFactory().getConnection().info("memory").get("used_memory")
            ),
            "timestamp", System.currentTimeMillis()
        );
        
        return ResponseEntity.ok(health);
    }
}
