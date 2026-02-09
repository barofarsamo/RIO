package com.riyobox.repository;

import com.riyobox.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
    
    List<Movie> findByIsFeaturedTrue();
    
    List<Movie> findByIsSomaliOriginalTrue();
    
    List<Movie> findByCategoriesContaining(String category);
    
    @Query("{'title': {$regex: ?0, $options: 'i'}}")
    List<Movie> searchByTitle(String title);
    
    @Query("{'$or': [{'title': {$regex: ?0, $options: 'i'}}, {'description': {$regex: ?0, $options: 'i'}}]}")
    List<Movie> searchMovies(String query);
    
    @Query(value = "{}", sort = "{'views': -1}")
    List<Movie> findTrendingMovies();
    
    @Query(value = "{}", sort = "{'createdAt': -1}")
    List<Movie> findNewReleases();
    
    Optional<Movie> findByIdAndIsFeaturedTrue(String id);
}
