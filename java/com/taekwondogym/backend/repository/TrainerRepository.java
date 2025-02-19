package com.taekwondogym.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taekwondogym.backend.model.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}

