package com.adobe.bookstore;

import com.adobe.bookstore.model.BookStock;
import com.adobe.bookstore.model.Order;
import com.adobe.bookstore.model.OrderedBook;
import com.adobe.bookstore.repository.BookStockRepository;
import com.adobe.bookstore.repository.OrderRepository;
import com.adobe.bookstore.repository.OrderedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books_stock/")
public class BookStockResource {

    private BookStockRepository bookStockRepository;
    private OrderRepository orderRepository;
    private OrderedBookRepository orderedBookRepository;

    @Autowired
    public BookStockResource(BookStockRepository bookStockRepository, OrderRepository orderRepository, OrderedBookRepository orderedBookRepository) {
        this.bookStockRepository = bookStockRepository;
        this.orderRepository = orderRepository;
        this.orderedBookRepository = orderedBookRepository;
    }

    @GetMapping("{bookId}")
    public ResponseEntity<BookStock> getStockById(@PathVariable String bookId) {
        return bookStockRepository.findById(bookId)
                .map(bookStock -> ResponseEntity.ok(bookStock))
                .orElse(ResponseEntity.notFound().build());
    }

    //receive order
    @PostMapping("order")
    public ResponseEntity<Integer> takeOrder(@RequestBody Order order) {
        //Order or = new Order();
        orderRepository.save(order);

        //For for checking quantity
        for (int i = 0; i < order.getOrderedbooks().size(); i++) {
            //get the id from the requestbody
            String idBook = order.getOrderedbooks().get(i).getBook().getId();
            //Find the book in the stock related to the id from the requestbody
            Optional<BookStock> bookStock = bookStockRepository.findById(idBook);

            //Quantity in stock
            int bookQuantity = bookStock.get().getQuantity();
            // Quantity Order
            int orderQuantity = order.getOrderedbooks().get(i).getQuantity();

            //save in the relation table
            OrderedBook orderedBook = new OrderedBook(bookStock.get(), orderQuantity, order);
            orderedBookRepository.save(orderedBook);

            //Look if there are enough stock quantity
            if (orderQuantity <= bookQuantity) {
                //bookStock.get().setQuantity(bookQuantity - orderQuantity);
                bookStockRepository.save(bookStock.get());

                //The order is valid
                order.setValidorder(true);
                orderRepository.save(order);

            } else {
                //The order is valid
                order.setValidorder(false);
                orderRepository.save(order);
                //not enough quantity, error 400
                return new ResponseEntity<Integer>(400, HttpStatus.BAD_REQUEST);

            }
        }

        //For for update quantity
        for (int i = 0; i < order.getOrderedbooks().size(); i++) {
            //get the id from the requestbody
            String idBook = order.getOrderedbooks().get(i).getBook().getId();
            //Find the book in the stock related to the id from the requestbody
            Optional<BookStock> bookStock = bookStockRepository.findById(idBook);

            //Quantity in stock
            int bookQuantity = bookStock.get().getQuantity();
            // Quantity Order
            int orderQuantity = order.getOrderedbooks().get(i).getQuantity();

            bookStock.get().setQuantity(bookQuantity - orderQuantity);
            bookStockRepository.save(bookStock.get());

            //The order is valid
            order.setValidorder(true);
            orderRepository.save(order);
        }

            //return order Id
            return new ResponseEntity<Integer>(order.getId(), HttpStatus.CREATED);

        }
    }

