package com.bookstore.bookstore_backend.controller;

import com.bookstore.bookstore_backend.dto.CartItemResponse;
import com.bookstore.bookstore_backend.entity.Book;
import com.bookstore.bookstore_backend.entity.CartItem;
import com.bookstore.bookstore_backend.repository.BookRepository;
import com.bookstore.bookstore_backend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;
    private final BookRepository bookRepository;

    public CartController(
            CartService cartService,
            BookRepository bookRepository) {

        this.cartService = cartService;
        this.bookRepository = bookRepository;
    }

    @PostMapping("/add/{bookId}")
    public ResponseEntity<?> addToCart(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "1") int quantity,
            Authentication authentication) {

        try {
            String email = authentication.getName();

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            if (quantity <= 0) {
                return ResponseEntity.badRequest()
                        .body("Quantity must be greater than 0");
            }

            CartItem cartItem = cartService.addToCart(
                    email,
                    book,
                    quantity
            );

            CartItemResponse response = new CartItemResponse(
                    cartItem.getId(),
                    cartItem.getBook().getId(),
                    cartItem.getBook().getTitle(),
                    cartItem.getBook().getAuthor(),
                    cartItem.getBook().getPrice(),
                    cartItem.getBook().getImage(),
                    cartItem.getQuantity()
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getCart(Authentication authentication) {

        String email = authentication.getName();

        List<CartItem> cartItems = cartService.getCartItems(email);

        List<CartItemResponse> response = cartItems.stream()
                .map(cartItem -> new CartItemResponse(
                        cartItem.getId(),
                        cartItem.getBook().getId(),
                        cartItem.getBook().getTitle(),
                        cartItem.getBook().getAuthor(),
                        cartItem.getBook().getPrice(),
                        cartItem.getBook().getImage(),
                        cartItem.getQuantity()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity,
            Authentication authentication) {

        try {
            String email = authentication.getName();

            CartItem cartItem = cartService.updateQuantity(
                    email,
                    cartItemId,
                    quantity
            );

            CartItemResponse response = new CartItemResponse(
                    cartItem.getId(),
                    cartItem.getBook().getId(),
                    cartItem.getBook().getTitle(),
                    cartItem.getBook().getAuthor(),
                    cartItem.getBook().getPrice(),
                    cartItem.getBook().getImage(),
                    cartItem.getQuantity()
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}