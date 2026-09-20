package com.bookstore.bookstore_backend.controller;

import com.bookstore.bookstore_backend.dto.AddressResponse;
import com.bookstore.bookstore_backend.entity.Address;
import com.bookstore.bookstore_backend.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<?> addAddress(
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String pincode,
            Authentication authentication) {

        try {
            String email = authentication.getName();

            Address savedAddress = addressService.addAddress(
                    email,
                    fullName,
                    phone,
                    address,
                    city,
                    state,
                    pincode
            );

            return ResponseEntity.ok(toResponse(savedAddress));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getMyAddresses(
            Authentication authentication) {

        String email = authentication.getName();

        List<AddressResponse> response =
                addressService.getUserAddresses(email)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<?> updateAddress(
            @PathVariable Long addressId,
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String pincode,
            Authentication authentication) {

        try {
            String email = authentication.getName();

            Address updatedAddress = addressService.updateAddress(
                    email,
                    addressId,
                    fullName,
                    phone,
                    address,
                    city,
                    state,
                    pincode
            );

            return ResponseEntity.ok(toResponse(updatedAddress));

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> deleteAddress(
            @PathVariable Long addressId,
            Authentication authentication) {

        try {
            String email = authentication.getName();

            addressService.deleteAddress(email, addressId);

            return ResponseEntity.ok("Address deleted successfully");

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    private AddressResponse toResponse(Address address) {

        return new AddressResponse(
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