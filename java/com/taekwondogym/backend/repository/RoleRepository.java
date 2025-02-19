package com.taekwondogym.backend.repository;

import com.taekwondogym.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name); // Accepts a String argument for the role name
}
