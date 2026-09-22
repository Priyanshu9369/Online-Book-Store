package com.bookstore.bookstore_backend.controller;

import com.bookstore.bookstore_backend.dto.OrderItemResponse;
import com.bookstore.bookstore_backend.dto.OrderResponse;
import com.bookstore.bookstore_backend.entity.Address;
import com.bookstore.bookstore_backend.entity.Order;
import com.bookstore.bookstore_backend.entity.OrderItem;
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
    @GetMapping("/admin")
public ResponseEntity<List<OrderResponse>> getAllOrders() {

    List<OrderResponse> response = orderService.getAllOrders()
            .stream()
            .map(this::toResponse)
            .toList();

    return ResponseEntity.ok(response);
}
    @GetMapping("/{orderId}")
public ResponseEntity<?> getOrderById(
        @PathVariable Long orderId,
        Authentication authentication) {

    try {
        String email = authentication.getName();

        Order order = orderService.getOrderById(
                email,
                orderId
        );

        return ResponseEntity.ok(toResponse(order));

    } catch (RuntimeException e) {
        return ResponseEntity
                .status(404)
                .body(e.getMessage());
    }
}
    @PutMapping("/{orderId}/cancel")
public ResponseEntity<?> cancelOrder(
        @PathVariable Long orderId,
        Authentication authentication) {

    try {
        String email = authentication.getName();

        Order order = orderService.cancelOrder(
                email,
                orderId
        );

        return ResponseEntity.ok(toResponse(order));

    } catch (RuntimeException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }
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

        List<OrderItemResponse> items = orderService
                .getOrderItems(order.getId())
                .stream()
                .map(this::toItemResponse)
                .toList();

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
                address.getPincode(),
                items
        );
    }

    private OrderItemResponse toItemResponse(OrderItem orderItem) {

        return new OrderItemResponse(
                orderItem.getBook().getId(),
                orderItem.getBook().getTitle(),
                orderItem.getBook().getAuthor(),
                orderItem.getBook().getImage(),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }
}