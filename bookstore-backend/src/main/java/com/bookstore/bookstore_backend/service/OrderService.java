package com.bookstore.bookstore_backend.service;

import com.bookstore.bookstore_backend.entity.Cart;
import com.bookstore.bookstore_backend.entity.CartItem;
import com.bookstore.bookstore_backend.entity.Order;
import com.bookstore.bookstore_backend.entity.OrderItem;
import com.bookstore.bookstore_backend.entity.User;
import com.bookstore.bookstore_backend.repository.CartItemRepository;
import com.bookstore.bookstore_backend.repository.CartRepository;
import com.bookstore.bookstore_backend.repository.OrderItemRepository;
import com.bookstore.bookstore_backend.repository.OrderRepository;
import com.bookstore.bookstore_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    public List<Order> getUserOrders(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user);
    }

    @Transactional
    public Order placeOrder(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = 0;

        for (CartItem cartItem : cartItems) {
            totalAmount +=
                    cartItem.getBook().getPrice()
                    * cartItem.getQuantity();
        }

        Order order = new Order(
                user,
                totalAmount,
                "PLACED"
        );

        Order savedOrder = orderRepository.save(order);

        for (CartItem cartItem : cartItems) {

            OrderItem orderItem = new OrderItem(
                    savedOrder,
                    cartItem.getBook(),
                    cartItem.getQuantity(),
                    cartItem.getBook().getPrice()
            );

            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }
}