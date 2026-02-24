package com.riyobox.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

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
    private String videoUrl;
    private Integer duration;
    private Integer releaseYear;
    private Double rating;
    @Indexed
    private List<String> categories = new ArrayList<>();
    private List<String> actors = new ArrayList<>();
    private String director;
    private Long views = 0L;
    private Long downloads = 0L;
    @Indexed
    private Boolean isFeatured = false;
    @Indexed
    private Boolean isSomaliOriginal = false;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Movie() {}

    public static MovieBuilder builder() { return new MovieBuilder(); }

    // Getters and Setters
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

    public static class MovieBuilder {
        private Movie m = new Movie();
        public MovieBuilder id(String id) { m.id = id; return this; }
        public MovieBuilder title(String title) { m.title = title; return this; }
        public MovieBuilder description(String d) { m.description = d; return this; }
        public MovieBuilder thumbnailUrl(String u) { m.thumbnailUrl = u; return this; }
        public MovieBuilder videoUrl(String u) { m.videoUrl = u; return this; }
        public MovieBuilder duration(Integer d) { m.duration = d; return this; }
        public MovieBuilder releaseYear(Integer y) { m.releaseYear = y; return this; }
        public MovieBuilder rating(Double r) { m.rating = r; return this; }
        public MovieBuilder categories(List<String> c) { m.categories = c; return this; }
        public MovieBuilder actors(List<String> a) { m.actors = a; return this; }
        public MovieBuilder director(String d) { m.director = d; return this; }
        public MovieBuilder views(Long v) { m.views = v; return this; }
        public MovieBuilder downloads(Long d) { m.downloads = d; return this; }
        public MovieBuilder isFeatured(Boolean f) { m.isFeatured = f; return this; }
        public MovieBuilder isSomaliOriginal(Boolean s) { m.isSomaliOriginal = s; return this; }
        public Movie build() { return m; }
    }
}
