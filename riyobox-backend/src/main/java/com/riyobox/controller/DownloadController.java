package com.riyobox.controller;

import com.riyobox.model.Download;
import com.riyobox.service.DownloadService;
import com.riyobox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/downloads")
@RequiredArgsConstructor
public class DownloadController {

    private final DownloadService downloadService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<Download>> getUserDownloads() {
        var user = userService.getCurrentUser();
        return ResponseEntity.ok(downloadService.getUserDownloads(user.getId()));
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Download>> getCompletedDownloads() {
        var user = userService.getCurrentUser();
        return ResponseEntity.ok(downloadService.getUserCompletedDownloads(user.getId()));
    }

    @PostMapping("/{movieId}")
    public ResponseEntity<Download> createDownload(
            @PathVariable String movieId,
            @RequestParam(defaultValue = "720p") String quality) {
        var user = userService.getCurrentUser();
        return ResponseEntity.ok(downloadService.createDownload(user.getId(), movieId, quality));
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<Download> updateDownloadProgress(
            @PathVariable String id,
            @RequestParam Integer progress) {
        return ResponseEntity.ok(downloadService.updateDownloadProgress(id, progress));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeDownload(@PathVariable String id) {
        downloadService.completeDownload(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDownload(@PathVariable String id) {
        downloadService.deleteDownload(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllDownloads() {
        var user = userService.getCurrentUser();
        downloadService.deleteUserDownloads(user.getId());
        return ResponseEntity.ok().build();
    }
}
