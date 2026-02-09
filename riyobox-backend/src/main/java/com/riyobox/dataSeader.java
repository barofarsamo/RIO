package com.riyobox;

import com.riyobox.model.Category;
import com.riyobox.model.Movie;
import com.riyobox.repository.CategoryRepository;
import com.riyobox.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    
    private final CategoryRepository categoryRepository;
    private final MovieRepository movieRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        seedCategories();
        seedMovies();
    }
    
    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                Category.builder()
                    .name("Action")
                    .description("Action movies")
                    .icon("🎬")
                    .movieCount(0)
                    .build(),
                Category.builder()
                    .name("Comedy")
                    .description("Comedy movies")
                    .icon("😂")
                    .movieCount(0)
                    .build(),
                Category.builder()
                    .name("Drama")
                    .description("Drama movies")
                    .icon("🎭")
                    .movieCount(0)
                    .build(),
                Category.builder()
                    .name("Horror")
                    .description("Horror movies")
                    .icon("👻")
                    .movieCount(0)
                    .build(),
                Category.builder()
                    .name("Romance")
                    .description("Romance movies")
                    .icon("💘")
                    .movieCount(0)
                    .build(),
                Category.builder()
                    .name("Somali Originals")
                    .description("Somali movies")
                    .icon("🇸🇴")
                    .movieCount(0)
                    .build()
            );
            
            categoryRepository.saveAll(categories);
        }
    }
    
    private void seedMovies() {
        if (movieRepository.count() == 0) {
            List<Movie> movies = List.of(
                Movie.builder()
                    .title("Hooyo Macaan")
                    .description("A heartwarming Somali family drama")
                    .thumbnailUrl("https://picsum.photos/200/300?random=1")
                    .videoUrl("https://example.com/videos/hooyo-macaan.mp4")
                    .duration(120)
                    .releaseYear(2023)
                    .rating(4.5)
                    .categories(List.of("Drama", "Somali Originals"))
                    .actors(List.of("Actor 1", "Actor 2"))
                    .director("Director Name")
                    .views(1000L)
                    .downloads(500L)
                    .isFeatured(true)
                    .isSomaliOriginal(true)
                    .build(),
                Movie.builder()
                    .title("Mogadishu Nights")
                    .description("Action thriller set in Mogadishu")
                    .thumbnailUrl("https://picsum.photos/200/300?random=2")
                    .videoUrl("https://example.com/videos/mogadishu-nights.mp4")
                    .duration(110)
                    .releaseYear(2022)
                    .rating(4.2)
                    .categories(List.of("Action", "Thriller", "Somali Originals"))
                    .actors(List.of("Actor 3", "Actor 4"))
                    .director("Director Name 2")
                    .views(1500L)
                    .downloads(700L)
                    .isFeatured(false)
                    .isSomaliOriginal(true)
                    .build()
            );
            
            movieRepository.saveAll(movies);
        }
    }
}
