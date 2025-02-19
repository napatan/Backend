package com.taekwondogym.backend.repository;

import com.taekwondogym.backend.model.TrainingClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingClassRepository extends JpaRepository<TrainingClass, Long> {

    // Find a class by its name
    List<TrainingClass> findByName(String name);

    // Get all classes
    List<TrainingClass> findAll();
}
