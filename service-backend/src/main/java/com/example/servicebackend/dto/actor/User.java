package com.example.servicebackend.dto.actor;

import com.example.servicebackend.constants.UserType;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class User {

    @Id
    private String userId;
    private String name;
    private String externalIdentifier;
    private UserType userType;
    private String userName;
    private String password;

}
