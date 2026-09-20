package com.bookstore.bookstore_backend.service;

import com.bookstore.bookstore_backend.entity.Address;
import com.bookstore.bookstore_backend.entity.Cart;
import com.bookstore.bookstore_backend.entity.CartItem;
import com.bookstore.bookstore_backend.entity.Order;
import com.bookstore.bookstore_backend.entity.OrderItem;
import com.bookstore.bookstore_backend.entity.User;
import com.bookstore.bookstore_backend.repository.AddressRepository;
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
    private final AddressRepository addressRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            AddressRepository addressRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public List<Order> getUserOrders(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUser(user);
    }

    @Transactional
    public Order placeOrder(String email, Long addressId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot use this address");
        }

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
                address,
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