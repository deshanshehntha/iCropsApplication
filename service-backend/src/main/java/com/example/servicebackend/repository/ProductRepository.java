package com.example.servicebackend.repository;

import com.example.servicebackend.dto.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    Product getProductByProductId(String productId);

}
