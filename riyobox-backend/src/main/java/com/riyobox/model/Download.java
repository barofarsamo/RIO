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
@Document(collection = "downloads")
public class Download {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    @Indexed
    private String movieId;
    
    private String movieTitle;
    private String thumbnailUrl;
    private Integer fileSize; // in MB
    private String quality;
    
    private LocalDateTime downloadedAt;
    private String downloadPath;
    
    @Builder.Default
    private Boolean isCompleted = false;
    
    @Builder.Default
    private Integer progress = 0; // percentage
    
    @PrePersist
    public void prePersist() {
        this.downloadedAt = LocalDateTime.now();
    }
}
