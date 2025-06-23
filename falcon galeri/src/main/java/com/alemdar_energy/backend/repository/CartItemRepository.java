package com.alemdar_energy.backend.repository;

import com.alemdar_energy.backend.model.Cart;
import com.alemdar_energy.backend.model.CartItem;
import com.alemdar_energy.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProduct(Cart cart, Product product);
    void deleteByCart(Cart cart);
}
