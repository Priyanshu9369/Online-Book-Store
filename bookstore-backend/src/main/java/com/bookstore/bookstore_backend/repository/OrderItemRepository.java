package com.bookstore.bookstore_backend.repository;

import com.bookstore.bookstore_backend.entity.Order;
import com.bookstore.bookstore_backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrder(Order order);
}