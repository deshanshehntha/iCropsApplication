package com.example.servicebackend.dto.product;

import com.example.servicebackend.dto.SupplierProductRelationship;
import com.example.servicebackend.dto.actor.Supplier;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Product {

    @Id
    private String productId;
    private String productName;
    private String description;
    private String externalIdentifier;
    private String imageLink;
    private String category;
    private String price;
    private SupplierProductRelationship supplier;
    private List<Product> recommendedProducts;

}
