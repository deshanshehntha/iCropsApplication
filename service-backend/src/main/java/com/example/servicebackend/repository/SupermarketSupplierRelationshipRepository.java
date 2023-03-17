package com.example.servicebackend.repository;

import com.example.servicebackend.dto.SupermarketSupplierRelationship;
import com.example.servicebackend.dto.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SupermarketSupplierRelationshipRepository extends
        MongoRepository<SupermarketSupplierRelationship, String> {

    List<SupermarketSupplierRelationship> findSupermarketSupplierRelationshipsBySupermarketId(String id);

    @Query(value = "{ 'supermarketId' : ?0 }", fields = "{ 'supplierId' : 1 }")
    List<SupermarketSupplierRelationship> findSupplierIdsBySupermarketId(String supermarketId);
}
