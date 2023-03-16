package com.example.servicebackend.constants;

import lombok.Data;

@Data
public class Response {
    private RequestStatus status;
    private String id;
    private String remark;
}
