package com.example.service_ui;

import com.example.service_ui.model.OrderLine;

import java.util.ArrayList;
import java.util.List;

public class OrderSingleton {

    private static OrderSingleton INSTANCE = null;
    private static List<OrderLine> orderLines;

    private OrderSingleton() {
    }
    public static OrderSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (OrderSingleton.class) {
                if (INSTANCE == null) {
                    orderLines = new ArrayList<>();
                    INSTANCE = new OrderSingleton();
                }
            }
        }
        return INSTANCE;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLine(OrderLine orderLine) {
        orderLines.removeIf(ordLine -> ordLine.getProductId().equals(orderLine.getProductId()));
        orderLines.add(orderLine);
    }
}
