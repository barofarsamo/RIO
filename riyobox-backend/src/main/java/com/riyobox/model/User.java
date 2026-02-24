package com.riyobox.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

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
    private String subscriptionPlan = "free";
    
    private List<WatchHistory> watchHistory = new ArrayList<>();
    private List<String> favorites = new ArrayList<>();
    private List<DownloadHistory> downloadHistory = new ArrayList<>();
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    public User() {}

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    // Manual Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
    public String getSubscriptionPlan() { return subscriptionPlan; }
    public void setSubscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }
    public List<WatchHistory> getWatchHistory() { return watchHistory; }
    public void setWatchHistory(List<WatchHistory> watchHistory) { this.watchHistory = watchHistory; }
    public List<String> getFavorites() { return favorites; }
    public void setFavorites(List<String> favorites) { this.favorites = favorites; }
    public List<DownloadHistory> getDownloadHistory() { return downloadHistory; }
    public void setDownloadHistory(List<DownloadHistory> downloadHistory) { this.downloadHistory = downloadHistory; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

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

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public static class UserBuilder {
        private User user = new User();
        public UserBuilder id(String id) { user.id = id; return this; }
        public UserBuilder email(String email) { user.email = email; return this; }
        public UserBuilder password(String password) { user.password = password; return this; }
        public UserBuilder name(String name) { user.name = name; return this; }
        public UserBuilder subscriptionPlan(String plan) { user.subscriptionPlan = plan; return this; }
        public UserBuilder enabled(boolean enabled) { user.enabled = enabled; return this; }
        public User build() { return user; }
    }
    
    public static class WatchHistory {
        private String movieId;
        private LocalDateTime watchedAt;
        private Integer progress;

        public WatchHistory() {}
        public String getMovieId() { return movieId; }
        public void setMovieId(String movieId) { this.movieId = movieId; }
        public LocalDateTime getWatchedAt() { return watchedAt; }
        public void setWatchedAt(LocalDateTime watchedAt) { this.watchedAt = watchedAt; }
        public Integer getProgress() { return progress; }
        public void setProgress(Integer progress) { this.progress = progress; }

        public static WatchHistoryBuilder builder() { return new WatchHistoryBuilder(); }
        public static class WatchHistoryBuilder {
            private WatchHistory wh = new WatchHistory();
            public WatchHistoryBuilder movieId(String id) { wh.movieId = id; return this; }
            public WatchHistoryBuilder watchedAt(LocalDateTime at) { wh.watchedAt = at; return this; }
            public WatchHistoryBuilder progress(Integer p) { wh.progress = p; return this; }
            public WatchHistory build() { return wh; }
        }
    }
    
    public static class DownloadHistory {
        private String movieId;
        private LocalDateTime downloadedAt;
        private String quality;

        public DownloadHistory() {}
        public String getMovieId() { return movieId; }
        public void setMovieId(String movieId) { this.movieId = movieId; }
        public LocalDateTime getDownloadedAt() { return downloadedAt; }
        public void setDownloadedAt(LocalDateTime downloadedAt) { this.downloadedAt = downloadedAt; }
        public String getQuality() { return quality; }
        public void setQuality(String quality) { this.quality = quality; }

        public static DownloadHistoryBuilder builder() { return new DownloadHistoryBuilder(); }
        public static class DownloadHistoryBuilder {
            private DownloadHistory dh = new DownloadHistory();
            public DownloadHistoryBuilder movieId(String id) { dh.movieId = id; return this; }
            public DownloadHistoryBuilder downloadedAt(LocalDateTime at) { dh.downloadedAt = at; return this; }
            public DownloadHistoryBuilder quality(String q) { dh.quality = q; return this; }
            public DownloadHistory build() { return dh; }
        }
    }
}
