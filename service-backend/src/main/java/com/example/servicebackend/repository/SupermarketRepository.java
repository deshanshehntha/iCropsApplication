package com.example.servicebackend.repository;

import com.example.servicebackend.dto.Supermarket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupermarketRepository extends MongoRepository<Supermarket, String> {

    Supermarket findFirstBySupermarketName(String name);

    Supermarket findBySupermarketId(String id);
}