package com.taekwondogym.backend.service;

import com.taekwondogym.backend.model.TrainingClass;
import com.taekwondogym.backend.repository.TrainingClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingClassService {

    @Autowired
    private TrainingClassRepository trainingClassRepository;

    // Get all training classes
    public List<TrainingClass> getAllTrainingClasses() {
        return trainingClassRepository.findAll();
    }

    // Get a specific training class by name
    public TrainingClass getTrainingClassByName(String name) {
        return trainingClassRepository.findByName(name).stream().findFirst().orElse(null);
    }

    // Get a specific training class by ID
    public TrainingClass getTrainingClassById(Long id) {
        return trainingClassRepository.findById(id).orElse(null);
    }

    // Create or update a training class
    public TrainingClass saveOrUpdateTrainingClass(TrainingClass trainingClass) {
        return trainingClassRepository.save(trainingClass);
    }
    // Create a new training class
    public TrainingClass createTrainingClass(String name, String description, Double price, String schedule) {
        TrainingClass newClass = new TrainingClass();
        newClass.setName(name);
        newClass.setPrice(price);
        newClass.setDescription(description);
        newClass.setSchedule(schedule); // Assuming schedule is a string; adjust if needed.
        return trainingClassRepository.save(newClass);
    }
 // Update an existing training class
    public TrainingClass updateTrainingClass(Long id, TrainingClass updatedTrainingClass) {
        // Check if the class exists by id
        TrainingClass existingClass = trainingClassRepository.findById(id).orElse(null);

        if (existingClass != null) {
            existingClass.setName(updatedTrainingClass.getName());
            existingClass.setDescription(updatedTrainingClass.getDescription());
            existingClass.setPrice(updatedTrainingClass.getPrice());
            existingClass.setSchedule(updatedTrainingClass.getSchedule());

            // Save the updated class
            return trainingClassRepository.save(existingClass);
        }
        return null;  // Class not found, return null or throw exception if needed
    }

    // Delete a training class
    public void deleteTrainingClass(Long id) {
        trainingClassRepository.deleteById(id);
    }
}