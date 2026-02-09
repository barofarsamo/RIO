package com.riyobox.config;

import com.riyobox.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(
                    "/api/auth/**",
                    "/swagger-ui/**", 
                    "/v3/api-docs/**",
                    "/api-docs/**",
                    "/webjars/**",
                    "/error"
                ).permitAll()
                
                // Public media access (thumbnails, posters)
                .requestMatchers("/api/media/thumbnails/**").permitAll()
                .requestMatchers("/api/media/posters/**").permitAll()
                
                // Authenticated endpoints
                .requestMatchers("/api/stream/**").authenticated()
                .requestMatchers("/api/movies/**").authenticated()
                .requestMatchers("/api/categories/**").authenticated()
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/downloads/**").authenticated()
                .requestMatchers("/api/favorites/**").authenticated()
                .requestMatchers("/api/history/**").authenticated()
                
                // Admin endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/analytics/**").hasRole("ADMIN")
                .requestMatchers("/api/reports/**").hasRole("ADMIN")
                
                // WebSocket endpoint
                .requestMatchers("/ws/**").authenticated()
                
                // Fallback - require authentication for all other endpoints
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allowed origins for development and production
        configuration.setAllowedOrigins(Arrays.asList(
            // Development
            "http://localhost:3000",  // React frontend
            "http://localhost:8081",  // Vite dev server
            "http://localhost:4200",  // Angular (optional)
            "http://127.0.0.1:3000",
            "http://127.0.0.1:8081",
            
            // Android emulator
            "http://10.0.2.2:8080",
            "http://10.0.2.2:3000",
            "http://10.0.2.2:8081",
            
            // Local network (for real device testing)
            "http://192.168.0.*",
            "http://192.168.1.*",
            "http://192.168.*.*",
            
            // Production domains
            "https://riyobox.com",
            "https://www.riyobox.com",
            "https://admin.riyobox.com",
            "https://api.riyobox.com"
        ));
        
        // Allowed HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));
        
        // Allowed headers
        configuration.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "X-API-Key",
            "X-Device-Id",
            "X-Platform",
            "X-App-Version"
        ));
        
        // Exposed headers (important for streaming)
        configuration.setExposedHeaders(Arrays.asList(
            "Content-Range",
            "Accept-Ranges",
            "Content-Length",
            "Content-Type",
            "X-Total-Count",
            "X-Page",
            "X-Page-Size",
            "X-Total-Pages",
            "Authorization",
            "X-Refresh-Token"
        ));
        
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 1 hour cache for CORS preflight
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    // Additional security configurations
    
    @Bean
    public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        // Allow H2 Console access only in development
        if (Arrays.asList("dev", "local").contains(System.getProperty("spring.profiles.active"))) {
            http
                .securityMatcher("/h2-console/**")
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                    .frameOptions(frame -> frame.sameOrigin())
                );
        }
        return http.build();
    }
    
    // Rate limiting filter bean (if you implement rate limiting)
    /*
    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registrationBean;
    }
    */
    
    // Additional security configuration for content security policy
    /*
    @Bean
    public SecurityFilterChain contentSecurityPolicyFilterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline';")
                )
                .frameOptions(frame -> frame.deny())
                .xssProtection(xss -> xss.enable())
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)
                )
            );
        return http.build();
    }
    */
}
