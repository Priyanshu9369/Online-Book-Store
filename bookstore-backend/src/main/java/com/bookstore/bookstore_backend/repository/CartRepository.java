package com.bookstore.bookstore_backend.repository;

import com.bookstore.bookstore_backend.entity.Cart;
import com.bookstore.bookstore_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}