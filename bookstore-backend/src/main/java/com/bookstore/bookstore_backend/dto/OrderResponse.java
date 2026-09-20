package com.bookstore.bookstore_backend.dto;

public class OrderResponse {

    private Long id;
    private double totalAmount;
    private String status;

    public OrderResponse() {
    }

    public OrderResponse(
            Long id,
            double totalAmount,
            String status) {

        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }
}