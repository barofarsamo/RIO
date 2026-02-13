package com.riyobox.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movies")
public class Movie {

    @Id
    private String id;

    @TextIndexed
    private String title;

    @TextIndexed
    private String description;

    @Indexed
    private String thumbnailUrl;

    @Indexed
    private String posterUrl; // Large poster image

    @Indexed
    private String videoUrl; // Main video URL (for backward compatibility)

    @Indexed
    private List<String> videoQualities; // ["480p", "720p", "1080p", "4K"]

    @Indexed
    private Map<String, String> videoUrls; // {"480p": "url", "720p": "url", "1080p": "url"}

    private Integer duration; // in minutes
    private Integer releaseYear;
    private Double rating;

    @Indexed
    private List<String> categories;

    private List<String> actors;
    private String director;
    private String producer;
    private String writer;

    private Long views;
    private Long downloads;
    private Long likes;
    private Long dislikes;

    // Streaming information
    private Integer bitrate; // in kbps
    private String codec; // H.264, H.265, VP9
    private String container; // mp4, mkv, webm

    // Subtitles and audio
    private Boolean hasSubtitles;
    private List<String> subtitleLanguages;
    private List<String> audioLanguages;
    private String defaultAudioLanguage;

    // Content rating
    private String ageRating; // G, PG, PG-13, R, NC-17
    private List<String> contentWarnings; // ["Violence", "Language", "Nudity"]

    @Indexed
    private Boolean isFeatured;

    @Indexed
    private Boolean isSomaliOriginal;

    @Indexed
    private Boolean isTrending;

    @Indexed
    private Boolean isExclusive;

    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    // SEO and discovery
    private List<String> keywords;
    private String metaDescription;
    private String slug;

    // Analytics
    private Double watchCompletionRate; // percentage
    private Integer averageWatchTime; // in minutes

    // Manual Getters/Setters for Lombok failure
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public List<String> getCategories() { return categories; }
    public void setCategories(List<String> categories) { this.categories = categories; }
    public List<String> getActors() { return actors; }
    public void setActors(List<String> actors) { this.actors = actors; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public Long getViews() { return views != null ? views : 0L; }
    public void setViews(Long views) { this.views = views; }
    public Long getDownloads() { return downloads != null ? downloads : 0L; }
    public void setDownloads(Long downloads) { this.downloads = downloads; }
    public Boolean getIsFeatured() { return isFeatured != null ? isFeatured : false; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }
    public Boolean getIsSomaliOriginal() { return isSomaliOriginal != null ? isSomaliOriginal : false; }
    public void setIsSomaliOriginal(Boolean isSomaliOriginal) { this.isSomaliOriginal = isSomaliOriginal; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Transient
    public String getBestQualityUrl() {
        if (videoUrls != null && !videoUrls.isEmpty()) {
            if (videoUrls.containsKey("1080p")) return videoUrls.get("1080p");
            if (videoUrls.containsKey("720p")) return videoUrls.get("720p");
            if (videoUrls.containsKey("480p")) return videoUrls.get("480p");
            return videoUrls.values().iterator().next();
        }
        return videoUrl;
    }

    public void incrementViews() {
        if (this.views == null) this.views = 0L;
        this.views++;
    }

    public void incrementDownloads() {
        if (this.downloads == null) this.downloads = 0L;
        this.downloads++;
    }

    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.views == null) this.views = 0L;
        if (this.downloads == null) this.downloads = 0L;
    }

    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static MovieBuilder builder() {
        return new MovieBuilder();
    }

    public static class MovieBuilder {
        private String title;
        private String description;
        private String thumbnailUrl;
        private String videoUrl;
        private Integer duration;
        private Integer releaseYear;
        private Double rating;
        private List<String> categories;
        private List<String> actors;
        private String director;
        private Long views;
        private Long downloads;
        private Boolean isFeatured;
        private Boolean isSomaliOriginal;

        public MovieBuilder title(String title) { this.title = title; return this; }
        public MovieBuilder description(String description) { this.description = description; return this; }
        public MovieBuilder thumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; return this; }
        public MovieBuilder videoUrl(String videoUrl) { this.videoUrl = videoUrl; return this; }
        public MovieBuilder duration(Integer duration) { this.duration = duration; return this; }
        public MovieBuilder releaseYear(Integer releaseYear) { this.releaseYear = releaseYear; return this; }
        public MovieBuilder rating(Double rating) { this.rating = rating; return this; }
        public MovieBuilder categories(List<String> categories) { this.categories = categories; return this; }
        public MovieBuilder actors(List<String> actors) { this.actors = actors; return this; }
        public MovieBuilder director(String director) { this.director = director; return this; }
        public MovieBuilder views(Long views) { this.views = views; return this; }
        public MovieBuilder downloads(Long downloads) { this.downloads = downloads; return this; }
        public MovieBuilder isFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; return this; }
        public MovieBuilder isSomaliOriginal(Boolean isSomaliOriginal) { this.isSomaliOriginal = isSomaliOriginal; return this; }

        public Movie build() {
            Movie m = new Movie();
            m.setTitle(title);
            m.setDescription(description);
            m.setThumbnailUrl(thumbnailUrl);
            m.setVideoUrl(videoUrl);
            m.setDuration(duration);
            m.setReleaseYear(releaseYear);
            m.setRating(rating);
            m.setCategories(categories);
            m.setActors(actors);
            m.setDirector(director);
            m.setViews(views);
            m.setDownloads(downloads);
            m.setIsFeatured(isFeatured);
            m.setIsSomaliOriginal(isSomaliOriginal);
            m.setCreatedAt(LocalDateTime.now());
            m.setUpdatedAt(LocalDateTime.now());
            return m;
        }
    }
}
