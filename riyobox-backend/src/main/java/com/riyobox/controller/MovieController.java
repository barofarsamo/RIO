package com.riyobox.controller;

import com.riyobox.model.Movie;
import com.riyobox.model.dto.MovieDTO;
import com.riyobox.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    
    private final MovieService movieService;
    
    @GetMapping
    public ResponseEntity<Page<Movie>> getMovies(Pageable pageable) {
        return ResponseEntity.ok(movieService.getMovies(pageable));
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movieService.toDTOList(movies));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable String id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movieService.toDTO(movie));
    }
    
    @GetMapping("/featured")
    public ResponseEntity<List<MovieDTO>> getFeaturedMovies() {
        List<Movie> movies = movieService.getFeaturedMovies();
        return ResponseEntity.ok(movieService.toDTOList(movies));
    }
    
    @GetMapping("/trending")
    public ResponseEntity<List<MovieDTO>> getTrendingMovies() {
        List<Movie> movies = movieService.getTrendingMovies();
        return ResponseEntity.ok(movieService.toDTOList(movies));
    }
    
    @GetMapping("/somali-originals")
    public ResponseEntity<List<MovieDTO>> getSomaliOriginals() {
        List<Movie> movies = movieService.getSomaliOriginals();
        return ResponseEntity.ok(movieService.toDTOList(movies));
    }
    
    @GetMapping("/new-releases")
    public ResponseEntity<List<MovieDTO>> getNewReleases() {
        List<Movie> movies = movieService.getNewReleases();
        return ResponseEntity.ok(movieService.toDTOList(movies));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(@RequestParam String q) {
        List<Movie> movies = movieService.searchMovies(q);
        return ResponseEntity.ok(movieService.toDTOList(movies));
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MovieDTO>> getMoviesByCategory(@PathVariable String category) {
        List<Movie> movies = movieService.getMoviesByCategory(category);
        return ResponseEntity.ok(movieService.toDTOList(movies));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.createMovie(movie));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> updateMovie(@PathVariable String id, @RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.updateMovie(id, movie));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/watch")
    public ResponseEntity<Void> recordWatch(@PathVariable String id) {
        movieService.recordWatch(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/favorite")
    public ResponseEntity<Void> toggleFavorite(@PathVariable String id) {
        // Implementation depends on user service
        return ResponseEntity.ok().build();
    }
}
