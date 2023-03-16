package com.example.servicebackend.repository;

import com.example.servicebackend.dto.actor.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findUserByUserName(String username);

    User findUserByUserId(String userId);

    User findUserByUserIdAndUserType(String userId, String userType);

    User findUserByUserNameAndPassword(String username, String password);

}
