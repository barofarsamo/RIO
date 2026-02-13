package com.riyobox.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

@Document(collection = "categories")
public class Category {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String name;
    
    private String description;
    private String icon;
    private Integer movieCount;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category() {}

    public Category(String id, String name, String description, String icon, Integer movieCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.movieCount = movieCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getMovieCount() { return movieCount; }
    public void setMovieCount(Integer movieCount) { this.movieCount = movieCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }

    public static class CategoryBuilder {
        private String id;
        private String name;
        private String description;
        private String icon;
        private Integer movieCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public CategoryBuilder id(String id) { this.id = id; return this; }
        public CategoryBuilder name(String name) { this.name = name; return this; }
        public CategoryBuilder description(String description) { this.description = description; return this; }
        public CategoryBuilder icon(String icon) { this.icon = icon; return this; }
        public CategoryBuilder movieCount(Integer movieCount) { this.movieCount = movieCount; return this; }
        public CategoryBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CategoryBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Category build() { return new Category(id, name, description, icon, movieCount, createdAt, updatedAt); }
    }
}
