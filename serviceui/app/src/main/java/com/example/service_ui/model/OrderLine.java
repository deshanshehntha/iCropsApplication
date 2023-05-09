package com.example.service_ui.model;

public class OrderLine {

    public String getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(String orderLineId) {
        this.orderLineId = orderLineId;
    }

    private String orderLineId;

    public String getOrderLineStatus() {
        return orderLineStatus;
    }

    public void setOrderLineStatus(String orderLineStatus) {
        this.orderLineStatus = orderLineStatus;
    }

    private String orderLineStatus;
    private String productId;
    private String productName;
    private String price;
    private String quantity;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
