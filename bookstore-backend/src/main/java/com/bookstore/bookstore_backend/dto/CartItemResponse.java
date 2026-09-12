package com.bookstore.bookstore_backend.dto;

public class CartItemResponse {

    private Long id;
    private Long bookId;
    private String title;
    private String author;
    private double price;
    private String image;
    private int quantity;

    public CartItemResponse() {
    }

    public CartItemResponse(
            Long id,
            Long bookId,
            String title,
            String author,
            double price,
            String image,
            int quantity) {

        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
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

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }
}