package com.adobe.bookstore;

import com.adobe.bookstore.model.BookStock;
import com.adobe.bookstore.model.Order;
import com.adobe.bookstore.model.OrderedBook;
import com.adobe.bookstore.repository.BookStockRepository;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookStockResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('12345-67890', 'some book', 7)")
    public void shouldReturnCurrentStock() {
        var result = restTemplate.getForObject("http://localhost:" + port + "/books_stock/12345-67890", BookStock.class);

        assertThat(result.getQuantity()).isEqualTo(7);
    }

    @Test
    public void ordersizeEqual() {
        // given
        OrderedBook order1 = new OrderedBook(null, 1, null);
        OrderedBook order2 = new OrderedBook(null, 1, null);
        Order order = new Order(new ArrayList<>(Arrays.asList(order1, order2)));
        // then
        assertThat(2).isEqualTo(order.getOrderedbooks().size());
    }
}
