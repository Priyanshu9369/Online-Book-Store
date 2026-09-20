package com.bookstore.bookstore_backend.repository;

import com.bookstore.bookstore_backend.entity.Address;
import com.bookstore.bookstore_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);
}