package com.riyobox.service;

import com.riyobox.model.WatchProgress;
import com.riyobox.repository.WatchProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WatchProgressService {
    
    private final WatchProgressRepository watchProgressRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SocketService socketService;
    
    public void saveProgress(String userId, String movieId, long position, long duration) {
        String key = String.format("progress:%s:%s", userId, movieId);
        redisTemplate.opsForHash().put(key, "position", position);
        redisTemplate.opsForHash().put(key, "duration", duration);
        redisTemplate.opsForHash().put(key, "updatedAt", LocalDateTime.now());
        redisTemplate.expire(key, 7, TimeUnit.DAYS);
        
        // Use socketService instead of messagingTemplate
        socketService.broadcastStats(Map.of(
            "userId", userId,
            "movieId", movieId,
            "position", position
        ));
        
        if (position % 30000 == 0) {
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
        String key = String.format("progress:%s:%s", userId, movieId);
        Map<Object, Object> redisData = redisTemplate.opsForHash().entries(key);
        
        if (!redisData.isEmpty()) {
            WatchProgress progress = new WatchProgress();
            progress.setUserId(userId);
            progress.setMovieId(movieId);
            progress.setPosition(((Number) redisData.get("position")).longValue());
            progress.setDuration(((Number) redisData.get("duration")).longValue());
            progress.setUpdatedAt((LocalDateTime) redisData.get("updatedAt"));
            return Optional.of(progress);
        }
        
        return watchProgressRepository.findByUserIdAndMovieId(userId, movieId);
    }
}
