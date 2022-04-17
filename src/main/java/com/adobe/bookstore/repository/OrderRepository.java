package com.adobe.bookstore.repository;

import com.adobe.bookstore.model.BookStock;
import com.adobe.bookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
