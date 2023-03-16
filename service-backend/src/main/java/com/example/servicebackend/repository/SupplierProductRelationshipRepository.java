package com.example.servicebackend.repository;

import com.example.servicebackend.dto.Supermarket;
import com.example.servicebackend.dto.SupplierProductRelationship;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SupplierProductRelationshipRepository extends MongoRepository<SupplierProductRelationship, String> {

    SupplierProductRelationship getSupplierProductRelationshipByProductId(String productId);
    List<SupplierProductRelationship> getSupplierProductRelationshipBySupplierId(String supplierId);

}
