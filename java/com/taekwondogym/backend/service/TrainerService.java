package com.taekwondogym.backend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taekwondogym.backend.model.Trainer;
import com.taekwondogym.backend.repository.TrainerRepository;
import com.taekwondogym.backend.store.TrainerImageStore;


@Service
public class TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TrainerImageStore trainerImageStore;

    // Save or update a trainer
    public Trainer saveTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    // Get all trainers
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    // Get trainer by ID
    public Trainer getTrainerById(Long id) {
        return trainerRepository.findById(id).orElse(null);
    }

    // Delete trainer by ID
    public void deleteTrainer(Long id) {
        trainerRepository.deleteById(id);
    }

    // Save trainer image
    public String saveTrainerImage(MultipartFile trainerImage) throws IOException {
        String filename = System.currentTimeMillis() + "_" + trainerImage.getOriginalFilename();
        trainerImageStore.setContent(filename, trainerImage.getInputStream());
        return filename;
    }
}
