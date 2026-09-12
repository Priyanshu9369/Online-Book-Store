package com.bookstore.bookstore_backend.repository;

import com.bookstore.bookstore_backend.entity.Cart;
import com.bookstore.bookstore_backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartAndBookId(Cart cart, Long bookId);
    List<CartItem> findByCart(Cart cart);
}