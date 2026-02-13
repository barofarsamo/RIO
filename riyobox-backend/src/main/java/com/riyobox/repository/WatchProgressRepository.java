package com.riyobox.repository;

import com.riyobox.model.WatchProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface WatchProgressRepository extends MongoRepository<WatchProgress, String> {
    Optional<WatchProgress> findByUserIdAndMovieId(String userId, String movieId);
}
