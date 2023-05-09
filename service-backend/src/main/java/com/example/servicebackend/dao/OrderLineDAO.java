package com.example.servicebackend.dao;

import com.example.servicebackend.constants.OrderStatus;
import com.example.servicebackend.dto.order.Order;
import com.example.servicebackend.dto.order.OrderLine;
import com.example.servicebackend.repository.OrderLineRepository;
import com.example.servicebackend.repository.OrderRepository;
import com.example.servicebackend.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Repository
public class OrderLineDAO {

    private final OrderLineRepository orderLineRepository;
    private final OrderRepository orderRepository;


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

        List<OrderLine> orderLines = orderLineRepository.findOrderLinesByOrderId(updateOrderLine.getOrderId());

       if (orderLines.stream().anyMatch(ol -> ol.getOrderStatus().equals(OrderStatus.IN_PROGRESS))) {
           Order order = orderRepository.findByOrderId(updateOrderLine.getOrderId());
           order.setOrderStatus(OrderStatus.IN_PROGRESS);
           orderRepository.save(order);
       }

        if (orderLines.stream().allMatch(ol -> ol.getOrderStatus().equals(OrderStatus.IN_TRANSIT))) {
            Order order = orderRepository.findByOrderId(updateOrderLine.getOrderId());
            order.setOrderStatus(OrderStatus.IN_TRANSIT);
            orderRepository.save(order);
        }

        if (orderLines.stream().allMatch(ol -> ol.getOrderStatus().equals(OrderStatus.READY))) {
            Order order = orderRepository.findByOrderId(updateOrderLine.getOrderId());
            order.setOrderStatus(OrderStatus.READY);
            orderRepository.save(order);
        }

        if (orderLines.stream().allMatch(ol -> ol.getOrderStatus().equals(OrderStatus.COMPLETED))) {
            Order order = orderRepository.findByOrderId(updateOrderLine.getOrderId());
            order.setOrderStatus(OrderStatus.READY);
            orderRepository.save(order);
        }

        return updateOrderLine.getOrderStatus().equals(orderLine.getOrderStatus());
    }

    public List<OrderLine> getOrderLinesBySupplierId(String supplierId) {
        return orderLineRepository.findOrderLinesBySupplierId(supplierId);
    }
}
