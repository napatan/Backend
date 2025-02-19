package com.taekwondogym.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.taekwondogym.backend.model.Trainer;
import com.taekwondogym.backend.service.TrainerService;
import com.taekwondogym.backend.store.TrainerImageStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainerImageStore trainerImageStore;

    // Add new trainer
    @PostMapping("/add")
    public ResponseEntity<String> addTrainer(
            @RequestParam("name") String name,
            @RequestParam("experience") String experience,
            @RequestParam(value = "awards", required = false) String awards,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            List<String> awardList = new ArrayList<>();
            if (awards != null && !awards.isEmpty()) {
                awardList = Arrays.asList(awards.split("\\s*,\\s*"));  // Split the string into a list
            }

            // Save image
            String imageFilename = null;
            if (image != null && !image.isEmpty()) {
                imageFilename = trainerService.saveTrainerImage(image);
            }

            // Create and save trainer
            Trainer trainer = new Trainer(name, experience, awardList, imageFilename);
            trainerService.saveTrainer(trainer);

            return ResponseEntity.status(HttpStatus.CREATED).body("Trainer added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    // Update an existing trainer
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTrainer(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("experience") String experience,
            @RequestParam(value = "awards", required = false) String awards,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            Trainer existingTrainer = trainerService.getTrainerById(id);
            if (existingTrainer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found.");
            }

            // Update trainer details
            existingTrainer.setName(name);
            existingTrainer.setExperience(experience);
            if (awards != null) {
                existingTrainer.setAwards(Arrays.asList(awards.split("\\s*,\\s*")));
            }

            // Handle image update
            if (image != null && !image.isEmpty()) {
                String imageFilename = trainerService.saveTrainerImage(image);
                existingTrainer.setImage(imageFilename);
            }

            trainerService.saveTrainer(existingTrainer);
            return ResponseEntity.ok("Trainer updated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    // Delete a trainer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrainer(@PathVariable Long id) {
        try {
            Trainer existingTrainer = trainerService.getTrainerById(id);
            if (existingTrainer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found.");
            }

            // Delete trainer image if exists
            if (existingTrainer.getImage() != null) {
                trainerImageStore.delete(existingTrainer.getImage());
            }

            trainerService.deleteTrainer(id);
            return ResponseEntity.ok("Trainer deleted successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image.");
        }
    }
    @GetMapping
    public List<Trainer> getAllTrainers() {
        return trainerService.getAllTrainers();
    }
    
}
