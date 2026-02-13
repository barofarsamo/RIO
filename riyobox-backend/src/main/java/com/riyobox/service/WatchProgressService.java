package com.riyobox.service;

import com.riyobox.model.WatchProgress;
import com.riyobox.repository.WatchProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WatchProgressService {
    
    private final WatchProgressRepository watchProgressRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    
    public void saveProgress(String userId, String movieId, long position, long duration) {
        // Save to Redis for real-time access
        String key = String.format("progress:%s:%s", userId, movieId);
        redisTemplate.opsForHash().put(key, "position", position);
        redisTemplate.opsForHash().put(key, "duration", duration);
        redisTemplate.opsForHash().put(key, "updatedAt", LocalDateTime.now());
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
        
        // Broadcast to WebSocket for real-time sync
        messagingTemplate.convertAndSendToUser(
            userId,
            "/queue/progress",
            Map.of(
                "movieId", movieId,
                "position", position,
                "duration", duration,
                "updatedAt", LocalDateTime.now()
            )
        );
        
        // Periodically save to MongoDB
        if (position % 30000 == 0) { // Save every 30 seconds
            WatchProgress progress = watchProgressRepository
                .findByUserIdAndMovieId(userId, movieId)
                .orElse(new WatchProgress(userId, movieId));
            
            progress.setPosition(position);
            progress.setDuration(duration);
            progress.setUpdatedAt(LocalDateTime.now());
            
            watchProgressRepository.save(progress);
        }
    }
    
    public Optional<WatchProgress> getProgress(String userId, String movieId) {
        // Try Redis first
        String key = String.format("progress:%s:%s", userId, movieId);
        Map<Object, Object> redisData = redisTemplate.opsForHash().entries(key);
        
        if (!redisData.isEmpty()) {
            WatchProgress progress = new WatchProgress();
            progress.setUserId(userId);
            progress.setMovieId(movieId);
            progress.setPosition((Long) redisData.get("position"));
            progress.setDuration((Long) redisData.get("duration"));
            progress.setUpdatedAt((LocalDateTime) redisData.get("updatedAt"));
            return Optional.of(progress);
        }
        
        // Fallback to MongoDB
        return watchProgressRepository.findByUserIdAndMovieId(userId, movieId);
    }
}
