package com.example.servicebackend.service;

import com.example.servicebackend.constants.RequestStatus;
import com.example.servicebackend.constants.Response;
import com.example.servicebackend.constants.UserType;
import com.example.servicebackend.dao.UserDAO;
import com.example.servicebackend.dto.actor.Customer;
import com.example.servicebackend.dto.actor.User;
import com.example.servicebackend.dto.actor.UserValidationDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserDAO userDAO;

    public Response createUser(User user) {

        String remark = validateUserCreationRequest(user);
        Response response;

        if (remark != null) {
            response = new Response();
            response.setStatus(RequestStatus.FAILED);
            response.setRemark(remark);
            return response;
        }

        User createdUser = userDAO.createUser(user);
        response = new Response();
        response.setStatus(RequestStatus.SUCCESS);
        response.setId(createdUser.getUserId());
        return response;
    }

    public User getUserByUserId(String userId) {
        if (userId == null) {
            return null;
        }
        return userDAO.getUserByUserId(userId);
    }

    public User getUserByUserIdAndType(String userId, UserType userType) {
        if (userId == null) {
            return null;
        }
        return userDAO.getUserByUserIdAndType(userId, userType);
    }

    private String validateUserCreationRequest(User user) {

        if (user.getUserName() != null
                && userDAO.isUsernameExists(user.getUserName())) {
            return new String("User name already exists");
        }

        if (user.getPassword() == null) {
            return new String("Enter a valid password");
        }

        return null;
    }

    public UserValidationDTO getValidUserByUsernameAndPassword(String username, String password) {
        User user = userDAO.getValidUserByUsernameAndPassword(username, password);
        UserValidationDTO userValidationDTO = null;
        if (user == null) {
            userValidationDTO = new UserValidationDTO();
            userValidationDTO.setRequestStatus(RequestStatus.FAILED);
            return userValidationDTO;
        } else {
            userValidationDTO = new UserValidationDTO();
            BeanUtils.copyProperties(user, userValidationDTO);
            userValidationDTO.setRequestStatus(RequestStatus.SUCCESS);
            return userValidationDTO;
        }
    }

    public Response updateCustomer(Customer customerToBeUpdated) {

        Customer customer = userDAO.getUserByUserId(customerToBeUpdated.getUserId());

        User createdUser = userDAO.createUser(user);
        response = new Response();
        response.setStatus(RequestStatus.SUCCESS);
        response.setId(createdUser.getUserId());
        return response;
    }
}
