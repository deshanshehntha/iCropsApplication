package com.example.service_ui;

import com.example.service_ui.model.OrderLine;

import java.util.ArrayList;
import java.util.List;

public class OrderedProductHolder {

    private static OrderedProductHolder INSTANCE = null;
    private static List<OrderLine> orderLines;

    private OrderedProductHolder() {
    }
    public static OrderedProductHolder getInstance() {
        if (INSTANCE == null) {
            synchronized (OrderedProductHolder.class) {
                if (INSTANCE == null) {
                    orderLines = new ArrayList<>();
                    INSTANCE = new OrderedProductHolder();
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
