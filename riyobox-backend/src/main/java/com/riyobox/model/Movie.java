package com.riyobox.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

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
    private String posterUrl;

    @Indexed
    private String videoUrl;

    @Indexed
    private List<String> videoQualities = new ArrayList<>();

    @Indexed
    private Map<String, String> videoUrls = new HashMap<>();

    private Integer duration;
    private Integer releaseYear;
    private Double rating;

    @Indexed
    private List<String> categories = new ArrayList<>();

    private List<String> actors = new ArrayList<>();
    private String director;
    private String producer;
    private String writer;

    private Long views = 0L;
    private Long downloads = 0L;
    private Long likes = 0L;
    private Long dislikes = 0L;

    private Integer bitrate;
    private String codec;
    private String container;

    private Boolean hasSubtitles = false;
    private List<String> subtitleLanguages = new ArrayList<>();
    private List<String> audioLanguages = new ArrayList<>();
    private String defaultAudioLanguage;

    private String ageRating;
    private List<String> contentWarnings = new ArrayList<>();

    @Indexed
    private Boolean isFeatured = false;

    @Indexed
    private Boolean isSomaliOriginal = false;

    @Indexed
    private Boolean isTrending = false;

    @Indexed
    private Boolean isExclusive = false;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    private List<String> keywords = new ArrayList<>();
    private String metaDescription;
    private String slug;

    private Double watchCompletionRate = 0.0;
    private Integer averageWatchTime = 0;

    public Movie() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public List<String> getVideoQualities() { return videoQualities; }
    public void setVideoQualities(List<String> videoQualities) { this.videoQualities = videoQualities; }
    public Map<String, String> getVideoUrls() { return videoUrls; }
    public void setVideoUrls(Map<String, String> videoUrls) { this.videoUrls = videoUrls; }
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
    public Long getViews() { return views; }
    public void setViews(Long views) { this.views = views; }
    public Long getDownloads() { return downloads; }
    public void setDownloads(Long downloads) { this.downloads = downloads; }
    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }
    public Boolean getIsSomaliOriginal() { return isSomaliOriginal; }
    public void setIsSomaliOriginal(Boolean isSomaliOriginal) { this.isSomaliOriginal = isSomaliOriginal; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static MovieBuilder builder() {
        return new MovieBuilder();
    }

    public static class MovieBuilder {
        private String id;
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

        public MovieBuilder id(String id) { this.id = id; return this; }
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
            Movie movie = new Movie();
            movie.id = id;
            movie.title = title;
            movie.description = description;
            movie.thumbnailUrl = thumbnailUrl;
            movie.videoUrl = videoUrl;
            movie.duration = duration;
            movie.releaseYear = releaseYear;
            movie.rating = rating;
            movie.categories = categories;
            movie.actors = actors;
            movie.director = director;
            movie.views = views;
            movie.downloads = downloads;
            movie.isFeatured = isFeatured;
            movie.isSomaliOriginal = isSomaliOriginal;
            return movie;
        }
    }
}
