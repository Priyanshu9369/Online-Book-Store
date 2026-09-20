package com.bookstore.bookstore_backend.dto;

public class AddressResponse {

    private Long id;
    private String fullName;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String pincode;

    public AddressResponse() {
    }

    public AddressResponse(
            Long id,
            String fullName,
            String phone,
            String address,
            String city,
            String state,
            String pincode) {

        this.id = id;
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