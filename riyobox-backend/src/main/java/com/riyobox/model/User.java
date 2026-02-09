package com.riyobox.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String email;
    
    private String password;
    private String name;
    private String profilePicture;
    
    @Indexed
    private String subscriptionPlan; // free, premium, pro
    
    private List<WatchHistory> watchHistory;
    private List<String> favorites;
    private List<DownloadHistory> downloadHistory;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private Boolean enabled = true;
    
    @Builder.Default
    private Boolean accountNonExpired = true;
    
    @Builder.Default
    private Boolean accountNonLocked = true;
    
    @Builder.Default
    private Boolean credentialsNonExpired = true;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
    
    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.subscriptionPlan == null) this.subscriptionPlan = "free";
        if (this.watchHistory == null) this.watchHistory = List.of();
        if (this.favorites == null) this.favorites = List.of();
        if (this.downloadHistory == null) this.downloadHistory = List.of();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WatchHistory {
        private String movieId;
        private LocalDateTime watchedAt;
        private Integer progress; // percentage
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DownloadHistory {
        private String movieId;
        private LocalDateTime downloadedAt;
        private String quality;
    }
}
