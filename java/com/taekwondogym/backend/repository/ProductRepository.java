package com.taekwondogym.backend.repository;

import com.taekwondogym.backend.model.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByAvailable(boolean available);
	List<Product> findByStockLessThanEqual(int stock);
}
