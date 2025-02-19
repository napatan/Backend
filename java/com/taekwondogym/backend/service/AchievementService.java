package com.taekwondogym.backend.service;

import com.taekwondogym.backend.model.Achievement;
import com.taekwondogym.backend.repository.AchievementRepository;
import com.taekwondogym.backend.store.AchievementImageStore; // Updated import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private AchievementImageStore achievementImageStore; // Updated autowire

    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    public Achievement getAchievementById(Long id) {
        return achievementRepository.findById(id).orElse(null);
    }

    public Achievement addAchievement(Achievement achievement, List<MultipartFile> images) throws IOException {
        List<String> imagePaths = saveImages(images);
        achievement.setImagePaths(imagePaths); // Set the image paths in the achievement
        return achievementRepository.save(achievement);
    }

    private List<String> saveImages(List<MultipartFile> images) throws IOException {
        List<String> imagePaths = new ArrayList<>();
        if (images != null) {
            for (MultipartFile image : images) {
                if (image != null && !image.isEmpty()) {
                    String filename = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                    achievementImageStore.setContent(filename, image.getInputStream()); // Use the store for images
                    imagePaths.add(filename);
                }
            }
        }
        return imagePaths;
    }

    public Achievement updateAchievement(Long id, Achievement achievementDetails, List<MultipartFile> images) throws IOException {
        Achievement achievement = achievementRepository.findById(id).orElse(null);

        if (achievement != null) {
            // Update fields
            achievement.setTitle(achievementDetails.getTitle());
            achievement.setDescription(achievementDetails.getDescription());

            // If new images are uploaded, process them
            if (images != null && !images.isEmpty()) {
                // Delete old images
                for (String imagePath : achievement.getImagePaths()) {
                    deleteImage(imagePath);
                }

                // Save new images
                List<String> imagePaths = saveImages(images);
                achievement.setImagePaths(imagePaths); // Update the image paths
                System.out.println("Updated image paths: " + imagePaths); // Log the updated paths
            }

            // Save the updated achievement
            return achievementRepository.save(achievement);
        }
        return null;
    }


    private void deleteImage(String imagePath) {
        try {
            achievementImageStore.delete(imagePath);
        } catch (IOException e) {
            e.printStackTrace(); // You may want to handle this more gracefully
        }
    }

    public void deleteAchievement(Long id) {
        achievementRepository.deleteById(id); // Delete the achievement by ID
    }
}
