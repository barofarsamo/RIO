package com.riyobox.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    // Manual Getters/Setters for Lombok failure
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getMovieCount() { return movieCount != null ? movieCount : 0; }
    public void setMovieCount(Integer movieCount) { this.movieCount = movieCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.movieCount == null) this.movieCount = 0;
    }
    
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }

    public static class CategoryBuilder {
        private String name;
        private String description;
        private String icon;
        private Integer movieCount;

        public CategoryBuilder name(String name) { this.name = name; return this; }
        public CategoryBuilder description(String description) { this.description = description; return this; }
        public CategoryBuilder icon(String icon) { this.icon = icon; return this; }
        public CategoryBuilder movieCount(Integer movieCount) { this.movieCount = movieCount; return this; }

        public Category build() {
            Category c = new Category();
            c.setName(this.name);
            c.setDescription(this.description);
            c.setIcon(this.icon);
            c.setMovieCount(this.movieCount);
            c.setCreatedAt(LocalDateTime.now());
            c.setUpdatedAt(LocalDateTime.now());
            return c;
        }
    }
}
