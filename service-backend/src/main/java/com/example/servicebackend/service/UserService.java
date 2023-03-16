package com.example.servicebackend.service;

import com.example.servicebackend.constants.RequestStatus;
import com.example.servicebackend.constants.Response;
import com.example.servicebackend.constants.UserType;
import com.example.servicebackend.dao.UserDAO;
import com.example.servicebackend.dto.actor.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    public Response createCustomer(Customer customer) {

        String remark = validateUserCreationRequest(customer, UserType.CUSTOMER);
        Response response;

        if (remark != null) {
            response = new Response();
            response.setStatus(RequestStatus.FAILED);
            response.setRemark(remark);
            return response;
        }

        customer.setUserType(UserType.CUSTOMER);

        Customer createdUser = userDAO.createAndUpdateCustomer(customer);
        response = new Response();
        response.setStatus(RequestStatus.SUCCESS);
        response.setId(createdUser.getUserId());
        return response;
    }

    public Response updateCustomer(Customer customerToBeUpdated, String userId) {
        Customer customer = userDAO.getCustomerById(userId);
        Customer clonedCustomer = cloneCustomerUpdateRequest(customer, customerToBeUpdated);
        Customer updatedCustomer = userDAO.createAndUpdateCustomer(clonedCustomer);

        Response response = new Response();
        response.setStatus(RequestStatus.SUCCESS);
        response.setId(updatedCustomer.getUserId());
        return response;
    }

    private Customer cloneCustomerUpdateRequest(Customer persistedCustomer, Customer receivedCustomer) {
        Customer clonedCustomer = new Customer();
        BeanUtils.copyProperties(persistedCustomer, clonedCustomer);

        if (receivedCustomer.getPreferredSupermarket() != null) {
            clonedCustomer.setPreferredSupermarket(receivedCustomer.getPreferredSupermarket());
        }

        return clonedCustomer;
    }


    public Response createManager(Manager manager) {

        String remark = validateUserCreationRequest(manager, UserType.MANAGER);
        Response response;

        if (remark != null) {
            response = new Response();
            response.setStatus(RequestStatus.FAILED);
            response.setRemark(remark);
            return response;
        }

        manager.setUserType(UserType.MANAGER);

        Manager createdUser = userDAO.createAndUpdateManager(manager);
        response = new Response();
        response.setStatus(RequestStatus.SUCCESS);
        response.setId(createdUser.getUserId());
        return response;
    }

    public Response updateManager(Manager managerToBeUpdated, String userId) {
        Manager manager = userDAO.getManagerById(userId);
        Manager createdUser = userDAO.createAndUpdateManager(manager);

        Response response = new Response();
        response.setStatus(RequestStatus.SUCCESS);
        response.setId(createdUser.getUserId());
        return response;
    }

    public Response createSupplier(Supplier supplier) {

        String remark = validateUserCreationRequest(supplier, UserType.SUPPLIER);
        Response response;

        if (remark != null) {
            response = new Response();
            response.setStatus(RequestStatus.FAILED);
            response.setRemark(remark);
            return response;
        }

        supplier.setUserType(UserType.SUPPLIER);

        Supplier createdUser = userDAO.createAndUpdateSupplier(supplier);
        response = new Response();
        response.setStatus(RequestStatus.SUCCESS);
        response.setId(createdUser.getUserId());
        return response;
    }


    public Response updateSupplier(Supplier supplierToBeUpdated, String supplierId) {
        Supplier supplier = userDAO.getSupplierById(supplierId);
        Supplier createdUser = userDAO.createAndUpdateSupplier(supplier);

        Response response = new Response();
        response.setStatus(RequestStatus.SUCCESS);
        response.setId(createdUser.getUserId());
        return response;
    }

    public Customer getCustomerByUserId(String userId) {
        if (userId == null) {
            return null;
        }
        return userDAO.getCustomerById(userId);
    }

    public Supplier getSupplierByUserId(String userId) {
        if (userId == null) {
            return null;
        }
        return userDAO.getSupplierById(userId);
    }

    public Manager getManagerByUserId(String userId) {
        if (userId == null) {
            return null;
        }
        return userDAO.getManagerById(userId);
    }


    private String validateUserCreationRequest(User user, UserType userType) {

        if (user.getUserName() != null
                && userDAO.isUsernameExists(user.getUserName(), userType)) {
            return new String("User name already exists");
        }

        if (user.getPassword() == null) {
            return new String("Enter a valid password");
        }

        return null;
    }

    public UserValidationDTO getValidUserByUsernameAndPassword(String username, String password) {

        Customer customer = userDAO.getValidCustomerByUsernameAndPassword(username, password);
        if (customer != null) {
            return generateUserValidationDTO(customer);
        }

        Manager manager = userDAO.getValidManagerByUsernameAndPassword(username, password);
        if (manager != null) {
            return generateUserValidationDTO(manager);
        }

        Supplier supplier = userDAO.getValidSupplierByUsernameAndPassword(username, password);
        if (supplier != null) {
            return generateUserValidationDTO(supplier);
        }

        UserValidationDTO userValidationDTO = new UserValidationDTO();
        userValidationDTO.setRequestStatus(RequestStatus.FAILED);
        return userValidationDTO;
    }

    private UserValidationDTO generateUserValidationDTO(User user) {
        UserValidationDTO userValidationDTO = new UserValidationDTO();
        BeanUtils.copyProperties(user, userValidationDTO);
        userValidationDTO.setRequestStatus(RequestStatus.SUCCESS);
        return userValidationDTO;
    }


}
