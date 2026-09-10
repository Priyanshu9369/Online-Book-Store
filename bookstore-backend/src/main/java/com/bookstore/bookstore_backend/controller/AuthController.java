package com.bookstore.bookstore_backend.controller;

import com.bookstore.bookstore_backend.dto.RegisterRequest;
import com.bookstore.bookstore_backend.dto.UserResponse;
import com.bookstore.bookstore_backend.entity.User;
import com.bookstore.bookstore_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        try {
            // Request DTO se User entity create kar rahe hain
            User user = new User(
                    request.getName(),
                    request.getEmail(),
                    request.getPassword()
            );

            // UserService password ko BCrypt se hash karke database mein save karega
            User registeredUser = userService.registerUser(user);

            // Response DTO banayenge
            // Isme password intentionally nahi hai
            UserResponse response = new UserResponse(
                    registeredUser.getId(),
                    registeredUser.getName(),
                    registeredUser.getEmail()
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}