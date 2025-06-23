package com.alemdar_energy.backend.repository;

import com.alemdar_energy.backend.model.Cart;
import com.alemdar_energy.backend.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
