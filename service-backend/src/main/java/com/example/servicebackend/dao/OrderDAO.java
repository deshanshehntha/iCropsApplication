package com.example.servicebackend.dao;

import com.example.servicebackend.constants.RequestStatus;
import com.example.servicebackend.constants.Response;
import com.example.servicebackend.dto.order.Order;
import com.example.servicebackend.dto.order.OrderLine;
import com.example.servicebackend.repository.OrderLineRepository;
import com.example.servicebackend.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Repository
public class OrderDAO {

    private final OrderRepository orderRepository;


    public Order createOrder(Order clonedOrder) {
        return orderRepository.save(clonedOrder);
    }

    public Order getOrderById(String id) {
        return orderRepository.findByOrderId(id);
    }


}
