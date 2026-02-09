package com.riyobox.service;

import com.riyobox.exception.BadRequestException;
import com.riyobox.exception.ConflictException;
import com.riyobox.model.User;
import com.riyobox.model.dto.AuthRequest;
import com.riyobox.model.dto.AuthResponse;
import com.riyobox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
    
    @Transactional
    public AuthResponse register(AuthRequest request) {
        // Validate email
        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new BadRequestException("Invalid email format");
        }
        
        // Validate password strength
        if (!PASSWORD_PATTERN.matcher(request.getPassword()).matches()) {
            throw new BadRequestException(
                "Password must be at least 8 characters with uppercase, lowercase, number and special character"
            );
        }
        
        // Validate name
        if (request.getName() == null || request.getName().trim().length() < 2) {
            throw new BadRequestException("Name must be at least 2 characters");
        }
        
        // Check if user exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already registered");
        }
        
        // Create new user
        User user = User.builder()
                .email(request.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName().trim())
                .subscriptionPlan("free")
                .enabled(true)
                .build();
        
        user = userRepository.save(user);
        
        // Generate token
        String token = jwtService.generateToken(user.getEmail());
        
        return AuthResponse.builder()
                .success(true)
                .token(token)
                .user(AuthResponse.UserDTO.fromUser(user))
                .message("Registration successful")
                .build();
    }
    
    public AuthResponse login(AuthRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                throw new BadRequestException("Email is required");
            }
            
            if (request.getPassword() == null || request.getPassword().isEmpty()) {
                throw new BadRequestException("Password is required");
            }
            
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase().trim(),
                            request.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Get user
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
            
            // Check if user is enabled
            if (!user.isEnabled()) {
                throw new BadCredentialsException("Account is disabled");
            }
            
            // Generate token
            String token = jwtService.generateToken(user.getEmail());
            
            return AuthResponse.builder()
                    .success(true)
                    .token(token)
                    .user(AuthResponse.UserDTO.fromUser(user))
                    .message("Login successful")
                    .build();
                    
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
    
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadCredentialsException("Not authenticated");
        }
        
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }
    
    public AuthResponse refreshToken(String oldToken) {
        try {
            String email = jwtService.extractUsername(oldToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadCredentialsException("User not found"));
            
            if (jwtService.validateToken(oldToken, user)) {
                String newToken = jwtService.generateToken(user.getEmail());
                
                return AuthResponse.builder()
                        .success(true)
                        .token(newToken)
                        .user(AuthResponse.UserDTO.fromUser(user))
                        .message("Token refreshed")
                        .build();
            } else {
                throw new BadCredentialsException("Invalid token");
            }
        } catch (Exception e) {
            throw new BadCredentialsException("Token refresh failed");
        }
    }
}
