package com.bookstore.bookstore_backend.controller;

import com.bookstore.bookstore_backend.dto.OrderResponse;
import com.bookstore.bookstore_backend.entity.Address;
import com.bookstore.bookstore_backend.entity.Order;
import com.bookstore.bookstore_backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(
            @RequestParam Long addressId,
            Authentication authentication) {

        try {
            String email = authentication.getName();

            Order order = orderService.placeOrder(
                    email,
                    addressId
            );

            return ResponseEntity.ok(toResponse(order));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    private OrderResponse toResponse(Order order) {

        Address address = order.getAddress();

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                address.getId(),
                address.getFullName(),
                address.getPhone(),
                address.getAddress(),
                address.getCity(),
                address.getState(),
                address.getPincode()
        );
    }
}