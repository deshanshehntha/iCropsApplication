package com.example.servicebackend.repository;

import com.example.servicebackend.dto.order.Order;
import com.example.servicebackend.dto.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    Order findByOrderId(String id);

    List<Order> findByCreateUserId(String userId);

    List<Order> findBySupermarketId(String supermarketId);
}
