package com.example.servicebackend.repository.user;

import com.example.servicebackend.dto.actor.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierRepository extends MongoRepository<Supplier, String> {

    Supplier findSupplierByUserName(String username);

    Supplier findSupplierByUserId(String userId);

    Supplier findSuppliersByUserNameAndPassword(String username, String password);
}
