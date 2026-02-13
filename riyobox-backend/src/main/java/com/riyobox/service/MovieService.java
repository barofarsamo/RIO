package com.riyobox.service;

import com.riyobox.model.Movie;
import com.riyobox.model.User;
import com.riyobox.model.dto.MovieDTO;
import com.riyobox.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {
    
    private final MovieRepository movieRepository;
    private final UserService userService;
    
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
    
    public Page<Movie> getMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }
    
    public Movie getMovieById(String id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }
    
    public List<Movie> getFeaturedMovies() {
        return movieRepository.findByIsFeaturedTrue();
    }
    
    public List<Movie> getTrendingMovies() {
        return movieRepository.findTrendingMovies();
    }
    
    public List<Movie> getSomaliOriginals() {
        return movieRepository.findByIsSomaliOriginalTrue();
    }
    
    public List<Movie> getNewReleases() {
        return movieRepository.findNewReleases();
    }
    
    public List<Movie> searchMovies(String query) {
        return movieRepository.searchMovies(query);
    }
    
    public List<Movie> getMoviesByCategory(String category) {
        return movieRepository.findByCategoriesContaining(category);
    }
    
    @Transactional
    public Movie createMovie(Movie movie) {
        movie.setCreatedAt(LocalDateTime.now());
        movie.setUpdatedAt(LocalDateTime.now());
        return movieRepository.save(movie);
    }
    
    @Transactional
    public Movie updateMovie(String id, Movie movieDetails) {
        Movie movie = getMovieById(id);
        
        movie.setTitle(movieDetails.getTitle());
        movie.setDescription(movieDetails.getDescription());
        movie.setThumbnailUrl(movieDetails.getThumbnailUrl());
        movie.setVideoUrl(movieDetails.getVideoUrl());
        movie.setDuration(movieDetails.getDuration());
        movie.setReleaseYear(movieDetails.getReleaseYear());
        movie.setCategories(movieDetails.getCategories());
        movie.setActors(movieDetails.getActors());
        movie.setDirector(movieDetails.getDirector());
        movie.setIsFeatured(movieDetails.getIsFeatured());
        movie.setIsSomaliOriginal(movieDetails.getIsSomaliOriginal());
        movie.setUpdatedAt(LocalDateTime.now());
        
        return movieRepository.save(movie);
    }
    
    @Transactional
    public void deleteMovie(String id) {
        Movie movie = getMovieById(id);
        movieRepository.delete(movie);
    }
    
    @Transactional
    public void recordWatch(String movieId) {
        Movie movie = getMovieById(movieId);
        movie.setViews(movie.getViews() + 1);
        movieRepository.save(movie);
        
        // Update user's watch history
        User user = userService.getCurrentUser();
        userService.addToWatchHistory(user.getId(), movieId);
    }
    
    @Transactional
    public void incrementDownloads(String movieId) {
        Movie movie = getMovieById(movieId);
        movie.setDownloads(movie.getDownloads() + 1);
        movieRepository.save(movie);
    }
    
    public MovieDTO toDTO(Movie movie) {
        MovieDTO dto = new MovieDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setThumbnailUrl(movie.getThumbnailUrl());
        dto.setVideoUrl(movie.getVideoUrl());
        dto.setDuration(movie.getDuration());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setRating(movie.getRating());
        dto.setCategories(movie.getCategories());
        dto.setActors(movie.getActors());
        dto.setDirector(movie.getDirector());
        dto.setViews(movie.getViews());
        dto.setDownloads(movie.getDownloads());
        dto.setIsFeatured(movie.getIsFeatured());
        dto.setIsSomaliOriginal(movie.getIsSomaliOriginal());
        dto.setCreatedAt(movie.getCreatedAt());
        return dto;
    }
    
    public List<MovieDTO> toDTOList(List<Movie> movies) {
        return movies.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
