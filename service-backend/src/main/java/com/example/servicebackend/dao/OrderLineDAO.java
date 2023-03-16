package com.example.servicebackend.dao;

import com.example.servicebackend.dto.order.Order;
import com.example.servicebackend.dto.order.OrderLine;
import com.example.servicebackend.repository.OrderLineRepository;
import com.example.servicebackend.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Repository
public class OrderLineDAO {

    private final OrderLineRepository orderLineRepository;

    public OrderLine createOrderLine(OrderLine orderLine) {
        return orderLineRepository.save(orderLine);
    }

    public List<OrderLine> getOrderLinesByOrderId(String orderId) {
        return orderLineRepository.findOrderLinesByOrderId(orderId);
    }

    public boolean updateOrderLine(OrderLine orderLine) {
        OrderLine orderLineToBeUpdated = orderLineRepository.findOrderLineByOrderLineId(orderLine.getOrderLineId());
        orderLineToBeUpdated.setOrderStatus(orderLine.getOrderStatus());
        OrderLine updateOrderLine = orderLineRepository.save(orderLineToBeUpdated);
        return updateOrderLine.getOrderStatus().equals(orderLine.getOrderStatus());
    }

    public List<OrderLine> getOrderLinesBySupplierId(String supplierId) {
        return orderLineRepository.findOrderLinesBySupplierId(supplierId);
    }
}
