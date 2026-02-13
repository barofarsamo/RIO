package com.riyobox.model.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
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

    // Manual Builder to bypass Lombok issues
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
            MovieDTO dto = new MovieDTO(id, title, description, thumbnailUrl, videoUrl, duration, releaseYear, rating, categories, actors, director, views, downloads, isFeatured, isSomaliOriginal, createdAt);
            return dto;
        }
    }

    public MovieDTO() {}
    public MovieDTO(String id, String title, String description, String thumbnailUrl, String videoUrl, Integer duration, Integer releaseYear, Double rating, List<String> categories, List<String> actors, String director, Long views, Long downloads, Boolean isFeatured, Boolean isSomaliOriginal, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.categories = categories;
        this.actors = actors;
        this.director = director;
        this.views = views;
        this.downloads = downloads;
        this.isFeatured = isFeatured;
        this.isSomaliOriginal = isSomaliOriginal;
        this.createdAt = createdAt;
    }
}
