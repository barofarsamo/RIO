package com.riyobox.controller;

import com.riyobox.model.dto.ApiResponse;
import com.riyobox.model.dto.AuthRequest;
import com.riyobox.model.dto.AuthResponse;
import com.riyobox.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(response, response.getMessage()));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, response.getMessage()));
    }
    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<AuthResponse.UserDTO>> getProfile() {
        var user = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(AuthResponse.UserDTO.fromUser(user)));
    }
}
