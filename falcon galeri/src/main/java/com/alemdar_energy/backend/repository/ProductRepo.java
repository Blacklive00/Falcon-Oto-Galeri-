package com.alemdar_energy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alemdar_energy.backend.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long>{
  
}
