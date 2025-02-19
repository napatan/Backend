package com.taekwondogym.backend.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // You can also use email if preferred
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id") // Foreign key to the Role table
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private Member member;
    
    // Constructors, getters, and setters
    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
