package com.riyobox.repository;

import com.riyobox.model.WatchProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchProgressRepository extends MongoRepository<WatchProgress, String> {
    Optional<WatchProgress> findByUserIdAndMovieId(String userId, String movieId);
}
