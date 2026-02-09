package com.riyobox.repository;

import com.riyobox.model.Download;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DownloadRepository extends MongoRepository<Download, String> {
    
    List<Download> findByUserId(String userId);
    
    List<Download> findByUserIdAndIsCompletedTrue(String userId);
    
    void deleteByUserId(String userId);
    
    void deleteByUserIdAndMovieId(String userId, String movieId);
}
