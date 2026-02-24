package com.riyobox.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
    private Integer movieCount = 0;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Category() {}
    public static CategoryBuilder builder() { return new CategoryBuilder(); }

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

    public static class CategoryBuilder {
        private Category c = new Category();
        public CategoryBuilder id(String id) { c.id = id; return this; }
        public CategoryBuilder name(String name) { c.name = name; return this; }
        public CategoryBuilder description(String d) { c.description = d; return this; }
        public CategoryBuilder icon(String i) { c.icon = i; return this; }
        public CategoryBuilder movieCount(Integer mc) { c.movieCount = mc; return this; }
        public Category build() { return c; }
    }
}
