package com.riyobox.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

@Document(collection = "watch_progress")
public class WatchProgress {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String movieId;

    private Long position;
    private Long duration;
    private LocalDateTime updatedAt;

    public WatchProgress() {}

    public WatchProgress(String userId, String movieId) {
        this.userId = userId;
        this.movieId = movieId;
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public Long getPosition() { return position; }
    public void setPosition(Long position) { this.position = position; }
    public Long getDuration() { return duration; }
    public void setDuration(Long duration) { this.duration = duration; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
