package com.riyobox.controller;

import com.riyobox.model.User;
import com.riyobox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }
    
    @GetMapping("/{id}/favorites")
    public ResponseEntity<List<String>> getUserFavorites(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserFavorites(id));
    }
    
    @PostMapping("/{id}/favorites/{movieId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable String id, @PathVariable String movieId) {
        userService.addToFavorites(id, movieId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}/favorites/{movieId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable String id, @PathVariable String movieId) {
        userService.removeFromFavorites(id, movieId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/watch-history")
    public ResponseEntity<List<User.WatchHistory>> getWatchHistory(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserWatchHistory(id));
    }
}
