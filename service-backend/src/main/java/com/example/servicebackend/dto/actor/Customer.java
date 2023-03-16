package com.example.servicebackend.dto.actor;

import com.example.servicebackend.dto.order.Order;
import lombok.Data;

import java.util.List;

@Data
public class Customer extends User {

    private List<String> preferredCategories;
    private List<Order> previousOrders;
    private Order orderToBeCheckedOut;
    private String preferredSupermarket;

}
