package com.riyobox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "watch_progress")
public class WatchProgress {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String movieId;

    private long position;
    private long duration;
    private LocalDateTime updatedAt;

    public WatchProgress(String userId, String movieId) {
        this.userId = userId;
        this.movieId = movieId;
        this.updatedAt = LocalDateTime.now();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public long getPosition() { return position; }
    public void setPosition(long position) { this.position = position; }
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
