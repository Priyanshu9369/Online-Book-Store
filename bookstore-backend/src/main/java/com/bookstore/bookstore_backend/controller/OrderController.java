package com.bookstore.bookstore_backend.controller;

import com.bookstore.bookstore_backend.entity.Order;
import com.bookstore.bookstore_backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.bookstore.bookstore_backend.dto.OrderResponse;


import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
public ResponseEntity<List<OrderResponse>> getMyOrders(
        Authentication authentication) {

    String email = authentication.getName();

    List<OrderResponse> response = orderService.getUserOrders(email)
            .stream()
            .map(order -> new OrderResponse(
                    order.getId(),
                    order.getTotalAmount(),
                    order.getStatus()
            ))
            .toList();

    return ResponseEntity.ok(response);
}
    @PostMapping("/place")
public ResponseEntity<?> placeOrder(
        Authentication authentication) {

    try {
        String email = authentication.getName();

        Order order = orderService.placeOrder(email);

        OrderResponse response = new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus()
        );

        return ResponseEntity.ok(response);

    } catch (RuntimeException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }
}
}
