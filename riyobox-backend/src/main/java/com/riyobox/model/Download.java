package com.riyobox.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

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
    private String downloadPath;
    private Boolean isCompleted;
    private Integer progress;
    private String downloadedAt;

    public Download() {}

    public Download(String id, String userId, String movieId, String movieTitle, String thumbnailUrl, Integer fileSize, String quality, String downloadPath, Boolean isCompleted, Integer progress, String downloadedAt) {
        this.id = id;
        this.userId = userId;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.thumbnailUrl = thumbnailUrl;
        this.fileSize = fileSize;
        this.quality = quality;
        this.downloadPath = downloadPath;
        this.isCompleted = isCompleted;
        this.progress = progress;
        this.downloadedAt = downloadedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public Integer getFileSize() { return fileSize; }
    public void setFileSize(Integer fileSize) { this.fileSize = fileSize; }
    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }
    public String getDownloadPath() { return downloadPath; }
    public void setDownloadPath(String downloadPath) { this.downloadPath = downloadPath; }
    public Boolean getIsCompleted() { return isCompleted; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }
    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public String getDownloadedAt() { return downloadedAt; }
    public void setDownloadedAt(String downloadedAt) { this.downloadedAt = downloadedAt; }

    public static DownloadBuilder builder() {
        return new DownloadBuilder();
    }

    public static class DownloadBuilder {
        private String id;
        private String userId;
        private String movieId;
        private String movieTitle;
        private String thumbnailUrl;
        private Integer fileSize;
        private String quality;
        private String downloadPath;
        private Boolean isCompleted;
        private Integer progress;
        private String downloadedAt;

        public DownloadBuilder id(String id) { this.id = id; return this; }
        public DownloadBuilder userId(String userId) { this.userId = userId; return this; }
        public DownloadBuilder movieId(String movieId) { this.movieId = movieId; return this; }
        public DownloadBuilder movieTitle(String movieTitle) { this.movieTitle = movieTitle; return this; }
        public DownloadBuilder thumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; return this; }
        public DownloadBuilder fileSize(Integer fileSize) { this.fileSize = fileSize; return this; }
        public DownloadBuilder quality(String quality) { this.quality = quality; return this; }
        public DownloadBuilder downloadPath(String downloadPath) { this.downloadPath = downloadPath; return this; }
        public DownloadBuilder isCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; return this; }
        public DownloadBuilder progress(Integer progress) { this.progress = progress; return this; }
        public DownloadBuilder downloadedAt(String downloadedAt) { this.downloadedAt = downloadedAt; return this; }

        public Download build() {
            return new Download(id, userId, movieId, movieTitle, thumbnailUrl, fileSize, quality, downloadPath, isCompleted, progress, downloadedAt);
        }
    }
}
