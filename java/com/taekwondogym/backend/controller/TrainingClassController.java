package com.taekwondogym.backend.controller;

import com.taekwondogym.backend.model.TrainingClass;
import com.taekwondogym.backend.service.TrainingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")  // This will map to /api/classes
public class TrainingClassController {

    @Autowired
    private TrainingClassService trainingClassService;

    // Get all classes
    @GetMapping
    public List<TrainingClass> getAllTrainingClasses() {
        return trainingClassService.getAllTrainingClasses();
    }

    // Get class by id
    @GetMapping("/{id}")
    public TrainingClass getTrainingClassById(@PathVariable Long id) {
        return trainingClassService.getTrainingClassById(id);
    }
    
 // Create a new training class (POST request)
    @PostMapping("/create")
    public TrainingClass createTrainingClass(@RequestBody TrainingClass trainingClass) {
        return trainingClassService.createTrainingClass(
            trainingClass.getName(), 
            trainingClass.getDescription(),
            trainingClass.getPrice(), 
            trainingClass.getSchedule()
        );
        }
 // Update an existing training class (PUT request)
    @PutMapping("/{id}")
    public TrainingClass updateTrainingClass(@PathVariable Long id, @RequestBody TrainingClass updatedTrainingClass) {
        // Retrieve the existing training class by ID
        TrainingClass existingClass = trainingClassService.getTrainingClassById(id);

        // Update fields if the class exists
        if (existingClass != null) {
            existingClass.setName(updatedTrainingClass.getName());
            existingClass.setDescription(updatedTrainingClass.getDescription());
            existingClass.setPrice(updatedTrainingClass.getPrice());
            existingClass.setSchedule(updatedTrainingClass.getSchedule());

            // Save the updated class
            return trainingClassService.saveOrUpdateTrainingClass(existingClass); // Use saveOrUpdateTrainingClass
        } else {
            throw new RuntimeException("Training class not found with id " + id);
        }
    }

    // Delete a training class (DELETE request)
    @DeleteMapping("/{id}")
    public String deleteTrainingClass(@PathVariable Long id) {
        TrainingClass existingClass = trainingClassService.getTrainingClassById(id);
        
        if (existingClass != null) {
            trainingClassService.deleteTrainingClass(id);
            return "Training class with id " + id + " was deleted successfully.";
        } else {
            throw new RuntimeException("Training class not found with id " + id);
        }
    }
}
