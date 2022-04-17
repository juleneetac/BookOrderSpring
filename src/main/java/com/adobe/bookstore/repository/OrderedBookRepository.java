package com.adobe.bookstore.repository;

import com.adobe.bookstore.model.Order;
import com.adobe.bookstore.model.OrderedBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedBookRepository extends JpaRepository<OrderedBook, Integer> {
}
