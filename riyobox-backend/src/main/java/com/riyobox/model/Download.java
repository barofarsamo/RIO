package com.riyobox.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "downloads")
public class Download {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    @Indexed
    private String movieId;
    
    private String movieTitle;
    private String thumbnailUrl;
    private Integer fileSize; // in MB
    private String quality;
    
    private LocalDateTime downloadedAt;
    private String downloadPath;
    
    @Builder.Default
    private Boolean isCompleted = false;
    
    @Builder.Default
    private Integer progress = 0; // percentage

    // Manual Getters/Setters for Lombok failure
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }

    public static DownloadBuilder builder() {
        return new DownloadBuilder();
    }

    public static class DownloadBuilder {
        private String userId;
        private String movieId;
        private String movieTitle;
        private String thumbnailUrl;
        private Integer fileSize;
        private String quality;
        private String downloadPath;
        private Boolean isCompleted = false;
        private Integer progress = 0;

        public DownloadBuilder userId(String userId) { this.userId = userId; return this; }
        public DownloadBuilder movieId(String movieId) { this.movieId = movieId; return this; }
        public DownloadBuilder movieTitle(String movieTitle) { this.movieTitle = movieTitle; return this; }
        public DownloadBuilder thumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; return this; }
        public DownloadBuilder fileSize(Integer fileSize) { this.fileSize = fileSize; return this; }
        public DownloadBuilder quality(String quality) { this.quality = quality; return this; }
        public DownloadBuilder downloadPath(String downloadPath) { this.downloadPath = downloadPath; return this; }
        public DownloadBuilder isCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; return this; }
        public DownloadBuilder progress(Integer progress) { this.progress = progress; return this; }

        public Download build() {
            Download d = new Download();
            d.userId = this.userId;
            d.movieId = this.movieId;
            d.movieTitle = this.movieTitle;
            d.thumbnailUrl = this.thumbnailUrl;
            d.fileSize = this.fileSize;
            d.quality = this.quality;
            d.downloadPath = this.downloadPath;
            d.isCompleted = this.isCompleted;
            d.progress = this.progress;
            d.downloadedAt = LocalDateTime.now();
            return d;
        }
    }
    
    public void prePersist() {
        this.downloadedAt = LocalDateTime.now();
    }
}
