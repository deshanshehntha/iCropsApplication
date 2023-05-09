package com.example.servicebackend.dto.order;

import com.example.servicebackend.constants.OrderStatus;
import com.example.servicebackend.dto.product.Product;
import com.example.servicebackend.dto.actor.Supplier;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class OrderLine {

    @Id
    private String orderLineId;
    private String orderId;
    private String productId;
    private String productName;
    private double quantity;
    private String supplierId;
    private String price;
    private OrderStatus orderStatus;
}
