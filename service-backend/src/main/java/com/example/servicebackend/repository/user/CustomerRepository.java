package com.example.servicebackend.repository.user;

import com.example.servicebackend.dto.actor.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    Customer findCustomerByUserName(String username);

    Customer findCustomerByUserId(String userId);

    Customer findCustomerByUserNameAndPassword(String username, String password);

}
