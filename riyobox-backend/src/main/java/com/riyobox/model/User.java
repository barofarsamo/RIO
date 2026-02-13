package com.riyobox.model;

import org.springframework.data.annotation.Id;
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
    private String subscriptionPlan; // free, premium, pro
    
    private List<WatchHistory> watchHistory = new ArrayList<>();
    private List<String> favorites = new ArrayList<>();
    private List<DownloadHistory> downloadHistory = new ArrayList<>();
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Boolean enabled = true;
    private Boolean accountNonExpired = true;
    private Boolean accountNonLocked = true;
    private Boolean credentialsNonExpired = true;
    
    public User() {}

    public User(String id, String email, String password, String name, String profilePicture, String subscriptionPlan, List<WatchHistory> watchHistory, List<String> favorites, List<DownloadHistory> downloadHistory, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean enabled, Boolean accountNonExpired, Boolean accountNonLocked, Boolean credentialsNonExpired) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.profilePicture = profilePicture;
        this.subscriptionPlan = subscriptionPlan;
        this.watchHistory = watchHistory != null ? watchHistory : new ArrayList<>();
        this.favorites = favorites != null ? favorites : new ArrayList<>();
        this.downloadHistory = downloadHistory != null ? downloadHistory : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.enabled = enabled != null ? enabled : true;
        this.accountNonExpired = accountNonExpired != null ? accountNonExpired : true;
        this.accountNonLocked = accountNonLocked != null ? accountNonLocked : true;
        this.credentialsNonExpired = credentialsNonExpired != null ? credentialsNonExpired : true;
    }

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

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private String id;
        private String email;
        private String password;
        private String name;
        private String profilePicture;
        private String subscriptionPlan;
        private List<WatchHistory> watchHistory;
        private List<String> favorites;
        private List<DownloadHistory> downloadHistory;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Boolean enabled;
        private Boolean accountNonExpired;
        private Boolean accountNonLocked;
        private Boolean credentialsNonExpired;

        public UserBuilder id(String id) { this.id = id; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder name(String name) { this.name = name; return this; }
        public UserBuilder profilePicture(String profilePicture) { this.profilePicture = profilePicture; return this; }
        public UserBuilder subscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; return this; }
        public UserBuilder watchHistory(List<WatchHistory> watchHistory) { this.watchHistory = watchHistory; return this; }
        public UserBuilder favorites(List<String> favorites) { this.favorites = favorites; return this; }
        public UserBuilder downloadHistory(List<DownloadHistory> downloadHistory) { this.downloadHistory = downloadHistory; return this; }
        public UserBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public UserBuilder enabled(Boolean enabled) { this.enabled = enabled; return this; }
        public UserBuilder accountNonExpired(Boolean accountNonExpired) { this.accountNonExpired = accountNonExpired; return this; }
        public UserBuilder accountNonLocked(Boolean accountNonLocked) { this.accountNonLocked = accountNonLocked; return this; }
        public UserBuilder credentialsNonExpired(Boolean credentialsNonExpired) { this.credentialsNonExpired = credentialsNonExpired; return this; }
        public User build() { return new User(id, email, password, name, profilePicture, subscriptionPlan, watchHistory, favorites, downloadHistory, createdAt, updatedAt, enabled, accountNonExpired, accountNonLocked, credentialsNonExpired); }
    }
    
    public static class WatchHistory {
        private String movieId;
        private LocalDateTime watchedAt;
        private Integer progress; // percentage

        public WatchHistory() {}
        public WatchHistory(String movieId, LocalDateTime watchedAt, Integer progress) {
            this.movieId = movieId;
            this.watchedAt = watchedAt;
            this.progress = progress;
        }
        public String getMovieId() { return movieId; }
        public void setMovieId(String movieId) { this.movieId = movieId; }
        public LocalDateTime getWatchedAt() { return watchedAt; }
        public void setWatchedAt(LocalDateTime watchedAt) { this.watchedAt = watchedAt; }
        public Integer getProgress() { return progress; }
        public void setProgress(Integer progress) { this.progress = progress; }
    }
    
    public static class DownloadHistory {
        private String movieId;
        private LocalDateTime downloadedAt;
        private String quality;

        public DownloadHistory() {}
        public DownloadHistory(String movieId, LocalDateTime downloadedAt, String quality) {
            this.movieId = movieId;
            this.downloadedAt = downloadedAt;
            this.quality = quality;
        }
        public String getMovieId() { return movieId; }
        public void setMovieId(String movieId) { this.movieId = movieId; }
        public LocalDateTime getDownloadedAt() { return downloadedAt; }
        public void setDownloadedAt(LocalDateTime downloadedAt) { this.downloadedAt = downloadedAt; }
        public String getQuality() { return quality; }
        public void setQuality(String quality) { this.quality = quality; }
    }
}
