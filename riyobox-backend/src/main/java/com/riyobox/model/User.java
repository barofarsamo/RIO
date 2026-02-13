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
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired != null ? accountNonExpired : true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked != null ? accountNonLocked : true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired != null ? credentialsNonExpired : true;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled != null ? enabled : true;
    }

    // Manual Getters for Lombok failure
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
    public String getSubscriptionPlan() { return subscriptionPlan; }
    public void setSubscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
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

    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.subscriptionPlan == null) this.subscriptionPlan = "free";
        if (this.watchHistory == null) this.watchHistory = new java.util.ArrayList<>();
        if (this.favorites == null) this.favorites = new java.util.ArrayList<>();
        if (this.downloadHistory == null) this.downloadHistory = new java.util.ArrayList<>();
    }
    
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private String email;
        private String password;
        private String name;
        private String subscriptionPlan;
        private Boolean enabled = true;

        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder name(String name) { this.name = name; return this; }
        public UserBuilder subscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; return this; }
        public UserBuilder enabled(Boolean enabled) { this.enabled = enabled; return this; }

        public User build() {
            User u = new User();
            u.setEmail(email);
            u.setPassword(password);
            u.setName(name);
            u.setSubscriptionPlan(subscriptionPlan);
            u.setEnabled(enabled);
            u.prePersist();
            return u;
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WatchHistory {
        private String movieId;
        private LocalDateTime watchedAt;
        private Integer progress; // percentage

        public String getMovieId() { return movieId; }
        public void setMovieId(String movieId) { this.movieId = movieId; }

        public static WatchHistoryBuilder builder() {
            return new WatchHistoryBuilder();
        }

        public static class WatchHistoryBuilder {
            private String movieId;
            private LocalDateTime watchedAt;
            private Integer progress;

            public WatchHistoryBuilder movieId(String movieId) { this.movieId = movieId; return this; }
            public WatchHistoryBuilder watchedAt(LocalDateTime watchedAt) { this.watchedAt = watchedAt; return this; }
            public WatchHistoryBuilder progress(Integer progress) { this.progress = progress; return this; }

            public WatchHistory build() {
                WatchHistory wh = new WatchHistory();
                wh.setMovieId(movieId);
                wh.watchedAt = watchedAt;
                wh.progress = progress;
                return wh;
            }
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DownloadHistory {
        private String movieId;
        private LocalDateTime downloadedAt;
        private String quality;

        public String getMovieId() { return movieId; }
        public void setMovieId(String movieId) { this.movieId = movieId; }

        public static DownloadHistoryBuilder builder() {
            return new DownloadHistoryBuilder();
        }

        public static class DownloadHistoryBuilder {
            private String movieId;
            private LocalDateTime downloadedAt;
            private String quality;

            public DownloadHistoryBuilder movieId(String movieId) { this.movieId = movieId; return this; }
            public DownloadHistoryBuilder downloadedAt(LocalDateTime downloadedAt) { this.downloadedAt = downloadedAt; return this; }
            public DownloadHistoryBuilder quality(String quality) { this.quality = quality; return this; }

            public DownloadHistory build() {
                DownloadHistory dh = new DownloadHistory();
                dh.setMovieId(movieId);
                dh.downloadedAt = downloadedAt;
                dh.quality = quality;
                return dh;
            }
        }
    }
}
