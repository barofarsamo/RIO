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

    @Transient
    public String getBestQualityUrl() {
        if (videoUrls != null && !videoUrls.isEmpty()) {
            // Try 1080p first, then 720p, then 480p
            if (videoUrls.containsKey("1080p")) return videoUrls.get("1080p");
            if (videoUrls.containsKey("720p")) return videoUrls.get("720p");
            if (videoUrls.containsKey("480p")) return videoUrls.get("480p");
            return videoUrls.values().iterator().next();
        }
        return videoUrl; // Fallback to legacy URL
    }

    @Transient
    public String getQualityUrl(String quality) {
        if (videoUrls != null && videoUrls.containsKey(quality)) {
            return videoUrls.get(quality);
        }
        return getBestQualityUrl(); // Fallback
    }

    @Transient
    public boolean isAvailableInQuality(String quality) {
        return videoUrls != null && videoUrls.containsKey(quality);
    }

    @Transient
    public boolean hasQuality(String quality) {
        return videoQualities != null && videoQualities.contains(quality);
    }

    public void incrementViews() {
        if (this.views == null) this.views = 0L;
        this.views++;
    }

    public void incrementDownloads() {
        if (this.downloads == null) this.downloads = 0L;
        this.downloads++;
    }

    public void incrementLikes() {
        if (this.likes == null) this.likes = 0L;
        this.likes++;
    }

    public void decrementLikes() {
        if (this.likes == null) this.likes = 0L;
        if (this.likes > 0) this.likes--;
    }

    public void incrementDislikes() {
        if (this.dislikes == null) this.dislikes = 0L;
        this.dislikes++;
    }

    public void decrementDislikes() {
        if (this.dislikes == null) this.dislikes = 0L;
        if (this.dislikes > 0) this.dislikes--;
    }

    public double getLikeDislikeRatio() {
        if (likes == null || dislikes == null || (likes + dislikes) == 0) {
            return 0.0;
        }
        return (double) likes / (likes + dislikes);
    }

    public String getFormattedDuration() {
        if (duration == null) return "0:00";
        int hours = duration / 60;
        int minutes = duration % 60;
        if (hours > 0) {
            return String.format("%d:%02d", hours, minutes);
        }
        return String.format("%d min", minutes);
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        if (this.publishedAt == null) {
            this.publishedAt = LocalDateTime.now();
        }
        
        // Initialize default values
        if (this.views == null) this.views = 0L;
        if (this.downloads == null) this.downloads = 0L;
        if (this.likes == null) this.likes = 0L;
        if (this.dislikes == null) this.dislikes = 0L;
        if (this.rating == null) this.rating = 0.0;
        if (this.isFeatured == null) this.isFeatured = false;
        if (this.isSomaliOriginal == null) this.isSomaliOriginal = false;
        if (this.isTrending == null) this.isTrending = false;
        if (this.isExclusive == null) this.isExclusive = false;
        if (this.hasSubtitles == null) this.hasSubtitles = false;
        if (this.watchCompletionRate == null) this.watchCompletionRate = 0.0;
        if (this.averageWatchTime == null) this.averageWatchTime = 0;
        
        // Initialize video qualities
        if (this.videoQualities == null) {
            this.videoQualities = List.of("480p", "720p");
        }
        
        // Initialize lists
        if (this.subtitleLanguages == null) this.subtitleLanguages = List.of();
        if (this.audioLanguages == null) this.audioLanguages = List.of("Somali", "Arabic", "English");
        if (this.contentWarnings == null) this.contentWarnings = List.of();
        if (this.keywords == null) this.keywords = List.of();
        
        // Set default audio language
        if (this.defaultAudioLanguage == null && this.audioLanguages != null && !this.audioLanguages.isEmpty()) {
            this.defaultAudioLanguage = this.audioLanguages.get(0);
        }
        
        // Generate slug if not present
        if (this.title != null && this.slug == null) {
            this.slug = generateSlug(this.title);
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        
        // Update slug if title changed
        if (this.title != null && this.slug == null) {
            this.slug = generateSlug(this.title);
        }
    }

    private String generateSlug(String title) {
        if (title == null) return null;
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }
}
