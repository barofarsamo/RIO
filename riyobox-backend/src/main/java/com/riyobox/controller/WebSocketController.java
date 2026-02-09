package com.riyobox.controller;

import com.riyobox.model.Movie;
import com.riyobox.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final MovieService movieService;
    
    @MessageMapping("/movie/add")
    @SendTo("/topic/movies")
    public Movie broadcastMovie(Movie movie) {
        return movie;
    }
    
    @MessageMapping("/admin/stats")
    @SendTo("/topic/admin/stats")
    public Map<String, Object> broadcastStats() {
        return Map.of(
            "activeUsers", getActiveUsers(),
            "currentViews", getCurrentViews(),
            "recentUploads", getRecentUploads()
        );
    }
    
    @MessageMapping("/user/activity")
    public void handleUserActivity(Map<String, Object> activity) {
        messagingTemplate.convertAndSend("/topic/user/activity", activity);
    }
    
    private int getActiveUsers() {
        // Implement active users tracking
        return (int) (Math.random() * 1000) + 500;
    }
    
    private int getCurrentViews() {
        // Implement current views tracking
        return (int) (Math.random() * 5000) + 1000;
    }
    
    private int getRecentUploads() {
        // Implement recent uploads tracking
        return (int) (Math.random() * 10) + 1;
    }
}
