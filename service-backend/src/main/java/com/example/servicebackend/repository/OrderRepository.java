package com.example.servicebackend.repository;

import com.example.servicebackend.dto.order.Order;
import com.example.servicebackend.dto.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

    Order findByOrderId(String id);
}
