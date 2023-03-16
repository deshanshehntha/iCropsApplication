package com.example.servicebackend.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class SupplierProductRelationship {

    @Id
    private String supplierProductRelationshipId;
    private String productId;
    private String supplierId;
}
