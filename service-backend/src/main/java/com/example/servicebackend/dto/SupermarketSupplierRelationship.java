package com.example.servicebackend.dto;

import lombok.Data;

@Data
public class SupermarketSupplierRelationship {

    private String supermarketSupplierRelationshipId;
    private String supermarketId;
    private String supplierId;
}
