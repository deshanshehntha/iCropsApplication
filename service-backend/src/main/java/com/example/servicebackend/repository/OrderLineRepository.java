package com.example.servicebackend.repository;

import com.example.servicebackend.dto.order.OrderLine;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderLineRepository extends MongoRepository<OrderLine, String> {

    List<OrderLine> findOrderLinesByOrderId(String orderId);
    OrderLine findOrderLineByOrderLineId(String orderLineId);

    List<OrderLine> findOrderLinesBySupplierId(String supplierId);

}
