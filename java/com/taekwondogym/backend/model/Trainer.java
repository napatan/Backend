package com.taekwondogym.backend.model;

import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String experience;
    private String image;

    @ElementCollection
    @CollectionTable(name = "trainer_awards", joinColumns = @JoinColumn(name = "trainer_id"))
    @Column(name = "award")
    private List<String> awards;

    // Constructors, Getters, and Setters

    public Trainer() {}

    public Trainer(String name, String experience, List<String> awards, String image) {
        this.name = name;
        this.experience = experience;
        this.awards = awards;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public List<String> getAwards() {
        return awards;
    }

    public void setAwards(List<String> awards) {
        this.awards = awards;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
