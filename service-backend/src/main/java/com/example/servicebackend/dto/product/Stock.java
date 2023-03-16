package com.example.servicebackend.dto.product;

import com.example.servicebackend.dto.Supermarket;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Stock {

    @Id
    private String stockId;
    private String productId;
    private double quantity;
    private Supermarket supermarket;
}
