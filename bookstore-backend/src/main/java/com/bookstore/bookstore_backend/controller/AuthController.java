package com.bookstore.bookstore_backend.controller;

import com.bookstore.bookstore_backend.dto.LoginRequest;
import com.bookstore.bookstore_backend.dto.RegisterRequest;
import com.bookstore.bookstore_backend.dto.UserResponse;
import com.bookstore.bookstore_backend.entity.User;
import com.bookstore.bookstore_backend.service.JwtService;
import com.bookstore.bookstore_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
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

            // Password response mein nahi bhejna hai
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            // Email aur password verify kar rahe hain
            User user = userService.loginUser(
                    request.getEmail(),
                    request.getPassword()
            );

            // Login successful hone par JWT token generate hoga
            String token = jwtService.generateToken(user.getEmail(), user.getRole());

            // Token frontend ko return karenge
            return ResponseEntity.ok(token);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
   
}