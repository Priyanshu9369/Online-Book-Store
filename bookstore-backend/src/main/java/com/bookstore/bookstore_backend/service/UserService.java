package com.bookstore.bookstore_backend.service;

import com.bookstore.bookstore_backend.entity.User;
import com.bookstore.bookstore_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
public User loginUser(String email, String password) {

    // Email se user database mein find karo
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));

    // Entered password ko database ke BCrypt password se compare karo
    if (!passwordEncoder.matches(password, user.getPassword())) {
        throw new RuntimeException("Invalid email or password");
    }

    // Login successful
    return user;
}
    public User registerUser(User user) {

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("CUSTOMER");

        // Save user in database
        return userRepository.save(user);
    }
}