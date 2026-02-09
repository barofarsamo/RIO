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
}
