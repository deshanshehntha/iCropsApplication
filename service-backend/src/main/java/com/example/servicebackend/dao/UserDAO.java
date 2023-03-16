package com.example.servicebackend.dao;

import com.example.servicebackend.constants.UserType;
import com.example.servicebackend.dto.actor.User;
import com.example.servicebackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class UserDAO {

    private final UserRepository userRepository;

    public User createUser(User user) {
        if (user != null) {
            return userRepository.save(user);
        }

        return null;
    }

    public boolean isUsernameExists(String username) {
        return userRepository.findUserByUserName(username) != null;
    }

    public User getUserByUserId(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    public User getUserByUserIdAndType(String userId, UserType userType) {
        return userRepository.findUserByUserIdAndUserType(userId, userType.toString());
    }

    public User getValidUserByUsernameAndPassword(String username, String password) {
        return userRepository.findUserByUserNameAndPassword(username, password);
    }
}
