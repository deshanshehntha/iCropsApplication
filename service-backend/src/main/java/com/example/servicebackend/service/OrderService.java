package com.example.servicebackend.service;

import com.example.servicebackend.constants.OrderStatus;
import com.example.servicebackend.constants.RequestStatus;
import com.example.servicebackend.constants.Response;
import com.example.servicebackend.dao.OrderDAO;
import com.example.servicebackend.dao.OrderLineDAO;
import com.example.servicebackend.dao.ProductDAO;
import com.example.servicebackend.dto.order.Order;
import com.example.servicebackend.dto.order.OrderLine;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderDAO orderDAO;
    private final OrderLineDAO orderLineDAO;
    private final ProductDAO productDAO;

    public Response createOrder(Order receivedOrder) {
        Response response = null;

        String remark = validateOrder(receivedOrder);
        if (remark != null) {
            response =  new Response();
            response.setRemark(remark);
            return response;
        }

        Order clonedOrder = new Order();
        cloneOrder(clonedOrder, receivedOrder);
        Order savedOrder = orderDAO.createOrder(clonedOrder);
        persistOrderLines(savedOrder.getOrderId(), receivedOrder.getOrderLines());

        response = new Response();
        response.setId(savedOrder.getOrderId());
        response.setStatus(RequestStatus.SUCCESS);
        return response;
    }

    private void persistOrderLines(String orderId, List<OrderLine> orderLines) {
        for (OrderLine orderLine: orderLines) {
            orderLine.setOrderId(orderId);
            setSupplierId(orderLine);
            orderLineDAO.createOrderLine(orderLine);
        }
    }

    private void setSupplierId(OrderLine orderLine) {
        orderLine.setSupplierId(productDAO.getSupplierIdByProductId(orderLine.getProductId()));
    }

    public Order getOrderById(String orderId) {
        Order order = orderDAO.getOrderById(orderId);
        List<OrderLine> orderLines = orderLineDAO.getOrderLinesByOrderId(orderId);
        order.setOrderLines(orderLines);
        return order;
    }

    public List<OrderLine> getOrderLinesBySupplierId(String supplierId) {
        return orderLineDAO.getOrderLinesBySupplierId(supplierId);
    }

    public Response updateOrderLines(OrderLine orderLine) {
        Response response = new Response();
        boolean isUpdateSuccessful = orderLineDAO.updateOrderLine(orderLine);
        response.setId(orderLine.getOrderLineId());
        if (isUpdateSuccessful) {
            response.setStatus(RequestStatus.SUCCESS);
        } else {
            response.setStatus(RequestStatus.FAILED);
        }
        return response;
    }

    private void cloneOrder(Order clonedOrder, Order receivedOrder) {
        BeanUtils.copyProperties(receivedOrder, clonedOrder);
        clonedOrder.setOrderLines(null);
        clonedOrder.setOrderStatus(OrderStatus.PENDING);

        receivedOrder.getOrderLines().forEach(orderLine -> orderLine.setOrderStatus(OrderStatus.PENDING));

    }

    private String validateOrder(Order receivedOrder) {

        if (!StringUtils.hasLength(receivedOrder.getCreateUserId())) {
            return "Order should contain created user id";
        }

        if (!StringUtils.hasLength(receivedOrder.getSupermarketId())) {
            return "Order should contain supermarket id";
        }

        if (CollectionUtils.isEmpty(receivedOrder.getOrderLines())) {
            return "Order should contain one or more orderlines";
        }

        for (OrderLine orderLine: receivedOrder.getOrderLines()) {
            if (!StringUtils.hasLength(orderLine.getProductId())) {
                return "Order line should contain product id";
            }
        }

        return null;
    }
}
