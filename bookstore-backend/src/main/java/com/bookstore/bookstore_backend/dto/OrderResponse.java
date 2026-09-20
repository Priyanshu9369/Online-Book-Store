package com.bookstore.bookstore_backend.dto;

public class OrderResponse {

    private Long id;
    private double totalAmount;
    private String status;

    private Long addressId;
    private String fullName;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String pincode;

    public OrderResponse() {
    }

    public OrderResponse(
            Long id,
            double totalAmount,
            String status,
            Long addressId,
            String fullName,
            String phone,
            String address,
            String city,
            String state,
            String pincode) {

        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
        this.addressId = addressId;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
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

    public Long getAddressId() {
        return addressId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }
}