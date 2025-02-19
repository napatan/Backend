package com.taekwondogym.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taekwondogym.backend.model.Order;
import com.taekwondogym.backend.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUser(User user);
	Optional<Order> findById(Long orderId);
}