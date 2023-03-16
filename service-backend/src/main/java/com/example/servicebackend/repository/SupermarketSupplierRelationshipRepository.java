package com.example.servicebackend.repository;

import com.example.servicebackend.dto.SupermarketSupplierRelationship;
import com.example.servicebackend.dto.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SupermarketSupplierRelationshipRepository extends
        MongoRepository<SupermarketSupplierRelationship, String> {

    List<SupermarketSupplierRelationship> findSupermarketSupplierRelationshipsBySupermarketId(String id);
}
