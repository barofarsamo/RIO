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

    public static MovieDTOBuilder builder() {
        return new MovieDTOBuilder();
    }

    public static class MovieDTOBuilder {
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

        public MovieDTOBuilder id(String id) { this.id = id; return this; }
        public MovieDTOBuilder title(String title) { this.title = title; return this; }
        public MovieDTOBuilder description(String description) { this.description = description; return this; }
        public MovieDTOBuilder thumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; return this; }
        public MovieDTOBuilder videoUrl(String videoUrl) { this.videoUrl = videoUrl; return this; }
        public MovieDTOBuilder duration(Integer duration) { this.duration = duration; return this; }
        public MovieDTOBuilder releaseYear(Integer releaseYear) { this.releaseYear = releaseYear; return this; }
        public MovieDTOBuilder rating(Double rating) { this.rating = rating; return this; }
        public MovieDTOBuilder categories(List<String> categories) { this.categories = categories; return this; }
        public MovieDTOBuilder actors(List<String> actors) { this.actors = actors; return this; }
        public MovieDTOBuilder director(String director) { this.director = director; return this; }
        public MovieDTOBuilder views(Long views) { this.views = views; return this; }
        public MovieDTOBuilder downloads(Long downloads) { this.downloads = downloads; return this; }
        public MovieDTOBuilder isFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; return this; }
        public MovieDTOBuilder isSomaliOriginal(Boolean isSomaliOriginal) { this.isSomaliOriginal = isSomaliOriginal; return this; }
        public MovieDTOBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public MovieDTO build() {
            MovieDTO dto = new MovieDTO();
            dto.id = id;
            dto.title = title;
            dto.description = description;
            dto.thumbnailUrl = thumbnailUrl;
            dto.videoUrl = videoUrl;
            dto.duration = duration;
            dto.releaseYear = releaseYear;
            dto.rating = rating;
            dto.categories = categories;
            dto.actors = actors;
            dto.director = director;
            dto.views = views;
            dto.downloads = downloads;
            dto.isFeatured = isFeatured;
            dto.isSomaliOriginal = isSomaliOriginal;
            dto.createdAt = createdAt;
            return dto;
        }
    }
}
