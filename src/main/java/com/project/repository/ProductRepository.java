package com.project.repository;

import com.project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
}