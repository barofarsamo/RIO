package com.riyobox.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MovieDTO {
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
    private LocalDateTime createdAt;

    public MovieDTO() {}

    public static MovieDTOBuilder builder() { return new MovieDTOBuilder(); }

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

    public static class MovieDTOBuilder {
        private MovieDTO dto = new MovieDTO();
        public MovieDTOBuilder id(String id) { dto.id = id; return this; }
        public MovieDTOBuilder title(String title) { dto.title = title; return this; }
        public MovieDTOBuilder description(String d) { dto.description = d; return this; }
        public MovieDTOBuilder thumbnailUrl(String u) { dto.thumbnailUrl = u; return this; }
        public MovieDTOBuilder videoUrl(String u) { dto.videoUrl = u; return this; }
        public MovieDTOBuilder duration(Integer d) { dto.duration = d; return this; }
        public MovieDTOBuilder releaseYear(Integer y) { dto.releaseYear = y; return this; }
        public MovieDTOBuilder rating(Double r) { dto.rating = r; return this; }
        public MovieDTOBuilder categories(List<String> c) { dto.categories = c; return this; }
        public MovieDTOBuilder actors(List<String> a) { dto.actors = a; return this; }
        public MovieDTOBuilder director(String d) { dto.director = d; return this; }
        public MovieDTOBuilder views(Long v) { dto.views = v; return this; }
        public MovieDTOBuilder downloads(Long d) { dto.downloads = d; return this; }
        public MovieDTOBuilder isFeatured(Boolean f) { dto.isFeatured = f; return this; }
        public MovieDTOBuilder isSomaliOriginal(Boolean s) { dto.isSomaliOriginal = s; return this; }
        public MovieDTOBuilder createdAt(LocalDateTime t) { dto.createdAt = t; return this; }
        public MovieDTO build() { return dto; }
    }
}
