package com.example.servicebackend.dto.order;

import com.example.servicebackend.constants.OrderStatus;
import com.example.servicebackend.constants.OrderType;
import com.example.servicebackend.dto.Supermarket;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {

    @Id
    private String orderId;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private String supermarketId;
    private String createUserId;
    private LocalDateTime orderedDate;
    private LocalDateTime completedDate;
    private List<OrderLine> orderLines;
}
