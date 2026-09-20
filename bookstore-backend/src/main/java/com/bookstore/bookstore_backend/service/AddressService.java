package com.bookstore.bookstore_backend.service;

import com.bookstore.bookstore_backend.entity.Address;
import com.bookstore.bookstore_backend.entity.User;
import com.bookstore.bookstore_backend.repository.AddressRepository;
import com.bookstore.bookstore_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(
            AddressRepository addressRepository,
            UserRepository userRepository) {

        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public Address addAddress(
            String email,
            String fullName,
            String phone,
            String address,
            String city,
            String state,
            String pincode) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address newAddress = new Address(
                user,
                fullName,
                phone,
                address,
                city,
                state,
                pincode
        );

        return addressRepository.save(newAddress);
    }

    public List<Address> getUserAddresses(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return addressRepository.findByUser(user);
    }

    public Address updateAddress(
            String email,
            Long addressId,
            String fullName,
            String phone,
            String address,
            String city,
            String state,
            String pincode) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!existingAddress.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot update this address");
        }

        existingAddress.setFullName(fullName);
        existingAddress.setPhone(phone);
        existingAddress.setAddress(address);
        existingAddress.setCity(city);
        existingAddress.setState(state);
        existingAddress.setPincode(pincode);

        return addressRepository.save(existingAddress);
    }

    public void deleteAddress(String email, Long addressId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!existingAddress.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot delete this address");
        }

        addressRepository.delete(existingAddress);
    }
}