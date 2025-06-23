package com.alemdar_energy.backend.service;

import com.alemdar_energy.backend.model.Cart;
import com.alemdar_energy.backend.model.CartItem;
import com.alemdar_energy.backend.model.Product;
import com.alemdar_energy.backend.model.User;
import com.alemdar_energy.backend.repository.CartRepository;
import com.alemdar_energy.backend.repository.ProductRepo;
import com.alemdar_energy.backend.repository.CartItemRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepo productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
            ProductRepo productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public Cart getCartByUser(User user) {
        // Kullanıcıya ait sepeti bulmaya çalışıyoruz
        Optional<Cart> cartOptional = cartRepository.findByUser(user);
        
        // Eğer sepet varsa, mevcut sepeti döndürüyoruz
        if (cartOptional.isPresent()) {
            return cartOptional.get();
        }
    
        // Sepet yoksa, yeni bir sepet oluşturup kaydediyoruz
        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart); // Yeni sepeti veritabanına kaydet ve geri döndür
    }
    

    // Sepete ürün ekle
    public void addItemToCart(User user, Long productId, int quantity) {
        Cart cart = getCartByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Ürün bulunamadı"));

        // Sepetteki ürünü kontrol et
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
    }

    // Sepetten ürün çıkar
    public void removeItemFromCart(User user, Long productId) {
        Cart cart = getCartByUser(user);
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            cartRepository.save(cart);
        }
    }

    // Sepeti temizle
    public void clearCart(User user) {
        Cart cart = getCartByUser(user);
        if (cart != null) {
            cart.getItems().clear(); // Sepetteki tüm öğeleri temizle
            cartRepository.save(cart); // Sepeti güncelle
        }
    }
}
