package com.taekwondogym.backend.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "training_classes")
public class TrainingClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., "Taekwondo Beginners"
    private String description; // e.g., "Perfect for beginners, includes basic techniques."
    private Double price; // e.g., 1500.00 (monthly fee)
    private String schedule; // e.g., "Mon, Wed, Fri 6:00 PM - 7:00 PM"


    
	public TrainingClass() {}
	

	public TrainingClass(String name, String description, Double price, String schedule) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.schedule = schedule;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
    
    
}