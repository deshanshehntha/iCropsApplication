package com.example.service_ui.model;

/**
 * A product entry in the list of products.
 */
public class Product {
    private static final String TAG = Product.class.getSimpleName();
    public final String productName;
    public final String imageLink;
    public final String price;
    public final String description;
    public final String productId;

    public Product(
            String productName, String imageLink, String price, String description, String productId) {
        this.productName = productName;
        this.imageLink = imageLink;
        this.price = price;
        this.description = description;
        this.productId = productId;
    }
}