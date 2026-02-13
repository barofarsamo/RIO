package com.riyobox.service;

import com.riyobox.model.User;
import com.riyobox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final AuthService authService;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
    
    public User getCurrentUser() {
        return authService.getCurrentUser();
    }
    
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public long getUserCount() {
        return userRepository.count();
    }
    
    public User updateUser(String id, User userDetails) {
        User user = getUserById(id);
        
        user.setName(userDetails.getName());
        user.setProfilePicture(userDetails.getProfilePicture());
        user.setSubscriptionPlan(userDetails.getSubscriptionPlan());
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    @Transactional
    public void addToFavorites(String userId, String movieId) {
        User user = getUserById(userId);
        
        if (!user.getFavorites().contains(movieId)) {
            user.getFavorites().add(movieId);
            userRepository.save(user);
        }
    }
    
    @Transactional
    public void removeFromFavorites(String userId, String movieId) {
        User user = getUserById(userId);
        user.getFavorites().remove(movieId);
        userRepository.save(user);
    }
    
    @Transactional
    public void addToWatchHistory(String userId, String movieId) {
        User user = getUserById(userId);
        
        // Remove existing entry if exists
        user.getWatchHistory().removeIf(wh -> wh.getMovieId().equals(movieId));
        
        // Add new entry
        User.WatchHistory watchHistory = User.WatchHistory.builder()
                .movieId(movieId)
                .watchedAt(LocalDateTime.now())
                .progress(0)
                .build();
        
        user.getWatchHistory().add(0, watchHistory); // Add to beginning
        userRepository.save(user);
    }
    
    @Transactional
    public void addToDownloadHistory(String userId, String movieId, String quality) {
        User user = getUserById(userId);
        
        User.DownloadHistory downloadHistory = User.DownloadHistory.builder()
                .movieId(movieId)
                .downloadedAt(LocalDateTime.now())
                .quality(quality)
                .build();
        
        user.getDownloadHistory().add(0, downloadHistory);
        userRepository.save(user);
    }
    
    public List<String> getUserFavorites(String userId) {
        User user = getUserById(userId);
        return user.getFavorites();
    }
    
    public List<User.WatchHistory> getUserWatchHistory(String userId) {
        User user = getUserById(userId);
        return user.getWatchHistory();
    }
}
