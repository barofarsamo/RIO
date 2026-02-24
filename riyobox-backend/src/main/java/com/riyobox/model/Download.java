package com.riyobox.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
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
    private Integer fileSize;
    private String quality;
    private String downloadPath;
    private Integer progress = 0;
    private boolean isCompleted = false;
    @CreatedDate
    private LocalDateTime createdAt;

    public Download() {}
    public static DownloadBuilder builder() { return new DownloadBuilder(); }

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
    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class DownloadBuilder {
        private Download d = new Download();
        public DownloadBuilder id(String id) { d.id = id; return this; }
        public DownloadBuilder userId(String userId) { d.userId = userId; return this; }
        public DownloadBuilder movieId(String movieId) { d.movieId = movieId; return this; }
        public DownloadBuilder movieTitle(String title) { d.movieTitle = title; return this; }
        public DownloadBuilder thumbnailUrl(String url) { d.thumbnailUrl = url; return this; }
        public DownloadBuilder fileSize(Integer size) { d.fileSize = size; return this; }
        public DownloadBuilder quality(String quality) { d.quality = quality; return this; }
        public DownloadBuilder downloadPath(String path) { d.downloadPath = path; return this; }
        public DownloadBuilder progress(Integer progress) { d.progress = progress; return this; }
        public DownloadBuilder isCompleted(boolean isCompleted) { d.isCompleted = isCompleted; return this; }
        public Download build() { return d; }
    }
}
