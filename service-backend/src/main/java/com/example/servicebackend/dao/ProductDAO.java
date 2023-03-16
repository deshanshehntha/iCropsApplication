package com.example.servicebackend.dao;

import com.example.servicebackend.dto.SupplierProductRelationship;
import com.example.servicebackend.dto.product.Product;
import com.example.servicebackend.repository.ProductRepository;
import com.example.servicebackend.repository.SupermarketRepository;
import com.example.servicebackend.repository.SupermarketSupplierRelationshipRepository;
import com.example.servicebackend.repository.SupplierProductRelationshipRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Repository
public class ProductDAO {

    private final SupplierProductRelationshipRepository supplierProductRelationshipRepository;
    private final ProductRepository productRepository;
    private final SupermarketSupplierRelationshipRepository supermarketSupplierRelationshipRepository;


    public String getSupplierIdByProductId(String productId) {
        SupplierProductRelationship supplierProductRelationship =
                supplierProductRelationshipRepository.getSupplierProductRelationshipByProductId(productId);
        return supplierProductRelationship.getSupplierId();
    }
}
