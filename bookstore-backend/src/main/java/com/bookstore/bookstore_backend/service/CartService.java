package com.bookstore.bookstore_backend.service;

import com.bookstore.bookstore_backend.entity.Book;
import com.bookstore.bookstore_backend.entity.Cart;
import com.bookstore.bookstore_backend.entity.CartItem;
import com.bookstore.bookstore_backend.entity.User;
import com.bookstore.bookstore_backend.repository.CartItemRepository;
import com.bookstore.bookstore_backend.repository.CartRepository;
import com.bookstore.bookstore_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    public Cart getOrCreateCart(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user)));
    }

    public CartItem addToCart(String email, Book book, int quantity) {

        Cart cart = getOrCreateCart(email);

        CartItem cartItem = cartItemRepository
                .findByCartAndBookId(cart, book.getId())
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem(cart, book, quantity);
        }

        return cartItemRepository.save(cartItem);
    }
    public java.util.List<CartItem> getCartItems(String email) {

    Cart cart = getOrCreateCart(email);

    return cartItemRepository.findByCart(cart);
}
public CartItem updateQuantity(String email, Long cartItemId, int quantity) {

    CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new RuntimeException("Cart item not found"));

    if (!cartItem.getCart().getUser().getEmail().equals(email)) {
        throw new RuntimeException("You cannot update this cart item");
    }

    if (quantity <= 0) {
        throw new RuntimeException("Quantity must be greater than 0");
    }

    cartItem.setQuantity(quantity);

    return cartItemRepository.save(cartItem);
}
}