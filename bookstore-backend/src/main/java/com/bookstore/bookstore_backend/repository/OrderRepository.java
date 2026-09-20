package com.bookstore.bookstore_backend.repository;

import com.bookstore.bookstore_backend.entity.Order;
import com.bookstore.bookstore_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}