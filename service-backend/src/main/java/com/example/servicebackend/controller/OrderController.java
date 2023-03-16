package com.example.servicebackend.controller;

import com.example.servicebackend.constants.Response;
import com.example.servicebackend.dto.order.Order;
import com.example.servicebackend.dto.order.OrderLine;
import com.example.servicebackend.dto.product.Product;
import com.example.servicebackend.service.OrderService;
import com.example.servicebackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity createOrder(@RequestBody Order order) {
        Response response = orderService.createOrder(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity getOrder(@PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PatchMapping ("/orderLines/{orderLineId}")
    public ResponseEntity updateOrderLine(@PathVariable String orderLineId, @RequestBody OrderLine orderLine) {
        orderLine.setOrderLineId(orderLineId);
        Response response = orderService.updateOrderLines(orderLine);
        return ResponseEntity.ok(response);
    }


    @GetMapping ("/orderLines/{supplierId}")
    public ResponseEntity getOrderLinesBySupplierId(@PathVariable String supplierId) {
        List<OrderLine> orderLines = orderService.getOrderLinesBySupplierId(supplierId);
        return ResponseEntity.ok(orderLines);
    }
}
