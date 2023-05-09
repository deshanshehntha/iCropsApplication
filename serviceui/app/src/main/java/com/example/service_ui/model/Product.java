package com.example.service_ui.model;

import java.util.List;

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
    public final List<Product> recommendedProducts;

    public Product(
            String productName, String imageLink, String price, String description, String productId,
            List<Product> recommendedProducts) {
        this.productName = productName;
        this.imageLink = imageLink;
        this.price = price;
        this.description = description;
        this.productId = productId;
        this.recommendedProducts = recommendedProducts;
    }
}