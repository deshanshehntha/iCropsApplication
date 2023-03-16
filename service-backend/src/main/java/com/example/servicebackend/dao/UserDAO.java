package com.example.servicebackend.dao;

import com.example.servicebackend.constants.UserType;
import com.example.servicebackend.dto.actor.Customer;
import com.example.servicebackend.dto.actor.Manager;
import com.example.servicebackend.dto.actor.Supplier;
import com.example.servicebackend.dto.actor.User;
import com.example.servicebackend.repository.user.CustomerRepository;
import com.example.servicebackend.repository.user.ManagerRepository;
import com.example.servicebackend.repository.user.SupplierRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class UserDAO {

    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final ManagerRepository managerRepository;

    public Customer createAndUpdateCustomer(Customer customer) {
        if (customer != null) {
            return customerRepository.save(customer);
        }

        return null;
    }

    public Supplier createAndUpdateSupplier(Supplier supplier) {
        if (supplier != null) {
            return supplierRepository.save(supplier);
        }

        return null;
    }

    public Manager createAndUpdateManager(Manager manager) {
        if (manager != null) {
            return managerRepository.save(manager);
        }

        return null;
    }

    public boolean isUsernameExists(String username, UserType userType) {
        if (UserType.CUSTOMER.equals(userType)) {
            return customerRepository.findCustomerByUserName((username)) != null;
        } else if (UserType.SUPPLIER.equals(userType)) {
            return supplierRepository.findSupplierByUserName(username) != null;
        } else if (UserType.MANAGER.equals(userType)) {
            return managerRepository.findManagerByUserName(username) != null;
        }
        return false;
    }

    public Customer getCustomerById(String userId) {
        return customerRepository.findCustomerByUserId(userId);
    }

    public Supplier getSupplierById(String userId) {
        return supplierRepository.findSupplierByUserId(userId);
    }

    public Manager getManagerById(String userId) {
        return managerRepository.findManagerByUserId(userId);
    }

    public Supplier getValidSupplierByUsernameAndPassword(String username, String password) {
        return supplierRepository.findSuppliersByUserNameAndPassword(username, password);
    }

    public Customer getValidCustomerByUsernameAndPassword(String username, String password) {
        return customerRepository.findCustomerByUserNameAndPassword(username, password);
    }

    public Manager getValidManagerByUsernameAndPassword(String username, String password) {
        return managerRepository.findManagerByUserNameAndPassword(username, password);
    }
}
