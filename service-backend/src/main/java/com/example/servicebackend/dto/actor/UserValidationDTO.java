package com.example.servicebackend.dto.actor;

import com.example.servicebackend.constants.RequestStatus;
import com.example.servicebackend.constants.UserType;
import lombok.Data;

@Data
public class UserValidationDTO {

    private String userId;
    private String name;
    private String externalIdentifier;
    private UserType userType;
    private RequestStatus requestStatus;

}
