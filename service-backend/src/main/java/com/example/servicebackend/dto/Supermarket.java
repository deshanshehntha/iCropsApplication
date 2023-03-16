package com.example.servicebackend.dto;

import com.example.servicebackend.dto.actor.Supplier;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "supermarket")
public class Supermarket {

    @Id
    private String supermarketId;
    private String supermarketName;
    private String location;
    private String managerId;
    private List<SupermarketSupplierRelationship> suppliers;
}
