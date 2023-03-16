package com.example.servicebackend.repository.user;

import com.example.servicebackend.dto.actor.Manager;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ManagerRepository extends MongoRepository<Manager, String> {

    Manager findManagerByUserName(String username);

    Manager findManagerByUserId(String userId);

    Manager findManagerByUserNameAndPassword(String username, String password);
}
