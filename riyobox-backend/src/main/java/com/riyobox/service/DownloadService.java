package com.riyobox.service;

import com.riyobox.model.Download;
import com.riyobox.repository.DownloadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DownloadService {
    
    private final DownloadRepository downloadRepository;
    private final UserService userService;
    private final MovieService movieService;
    
    public List<Download> getUserDownloads(String userId) {
        return downloadRepository.findByUserId(userId);
    }
    
    public List<Download> getUserCompletedDownloads(String userId) {
        return downloadRepository.findByUserIdAndIsCompletedTrue(userId);
    }
    
    public Download getDownloadById(String id) {
        return downloadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Download not found"));
    }
    
    @Transactional
    public Download createDownload(String userId, String movieId, String quality) {
        // Get movie details
        var movie = movieService.getMovieById(movieId);
        
        // Create download record
        Download download = Download.builder()
                .userId(userId)
                .movieId(movieId)
                .movieTitle(movie.getTitle())
                .thumbnailUrl(movie.getThumbnailUrl())
                .fileSize(calculateFileSize(quality))
                .quality(quality)
                .downloadPath(generateDownloadPath(userId, movieId, quality))
                .isCompleted(false)
                .progress(0)
                .build();
        
        download = downloadRepository.save(download);
        
        // Update user's download history
        userService.addToDownloadHistory(userId, movieId, quality);
        
        return download;
    }
    
    @Transactional
    public Download updateDownloadProgress(String id, Integer progress) {
        Download download = getDownloadById(id);
        download.setProgress(progress);
        
        if (progress >= 100) {
            download.setIsCompleted(true);
            // Increment movie download count
            movieService.incrementDownloads(download.getMovieId());
        }
        
        return downloadRepository.save(download);
    }
    
    @Transactional
    public void completeDownload(String id) {
        Download download = getDownloadById(id);
        download.setIsCompleted(true);
        download.setProgress(100);
        downloadRepository.save(download);
    }
    
    @Transactional
    public void deleteDownload(String id) {
        Download download = getDownloadById(id);
        downloadRepository.delete(download);
    }
    
    @Transactional
    public void deleteUserDownloads(String userId) {
        downloadRepository.deleteByUserId(userId);
    }
    
    private Integer calculateFileSize(String quality) {
        if ("480p".equals(quality)) return 500;
        if ("720p".equals(quality)) return 1000;
        if ("1080p".equals(quality)) return 2000;
        return 800;
    }
    
    private String generateDownloadPath(String userId, String movieId, String quality) {
        return String.format("/downloads/%s/%s_%s.mp4", userId, movieId, quality);
    }
}
