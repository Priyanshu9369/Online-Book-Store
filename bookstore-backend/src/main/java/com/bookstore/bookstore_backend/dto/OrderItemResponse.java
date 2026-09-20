package com.bookstore.bookstore_backend.dto;

public class OrderItemResponse {

    private Long bookId;
    private String title;
    private String author;
    private String image;
    private int quantity;
    private double price;

    public OrderItemResponse() {
    }

    public OrderItemResponse(
            Long bookId,
            String title,
            String author,
            String image,
            int quantity,
            double price) {

        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}