package com.taekwondogym.backend.controller;

import com.taekwondogym.backend.model.Achievement;
import com.taekwondogym.backend.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping
    public List<Achievement> getAllAchievements() {
        return achievementService.getAllAchievements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Achievement> getAchievementById(@PathVariable Long id) {
        Achievement achievement = achievementService.getAchievementById(id);
        return achievement != null ? ResponseEntity.ok(achievement) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    public ResponseEntity<Achievement> addAchievement(
            @RequestPart("achievement") Achievement achievement,
            @RequestPart(required = false, value = "images") List<MultipartFile> images) throws IOException {
        Achievement savedAchievement = achievementService.addAchievement(achievement, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAchievement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Achievement> updateAchievement(
            @PathVariable Long id,
            @RequestPart("achievement") Achievement achievementDetails,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            Achievement updatedAchievement = achievementService.updateAchievement(id, achievementDetails, images);
            return updatedAchievement != null ? ResponseEntity.ok(updatedAchievement) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
    }

    // Add a method to return the full URLs for the images
    private List<String> getFullImageUrls(Achievement achievement) {
        return achievement.getImagePaths().stream()
                .map(path -> "/uploads/images/achievements/" + path)
                .toList();
    }
    
}
