package com.example.servicebackend.repository;

import com.example.servicebackend.dto.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    Product getProductByProductId(String productId);

    @Query(value = "{ 'productId' : { $in : ?0 } }")
    List<Product> getProductsByProductIds(List<String> productIds);

}
