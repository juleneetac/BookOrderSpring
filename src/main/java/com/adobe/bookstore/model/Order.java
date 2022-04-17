package com.adobe.bookstore.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "order")
@JsonSerialize
public class Order{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "parentOrder")
    private List<OrderedBook> orderedbooks;

    @Column(name = "validorder", nullable = false)
    private boolean validorder;


    public Order(List<OrderedBook> orderedbooks) {
        this.orderedbooks = orderedbooks;
    }

    public Order() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<OrderedBook> getOrderedbooks() {
        return orderedbooks;
    }

    public void setOrderedbooks(List<OrderedBook> orderedbooks) {
        this.orderedbooks = orderedbooks;
    }

    public boolean isValidorder() {
        return validorder;
    }

    public void setValidorder(boolean validorder) {
        this.validorder = validorder;
    }
}
