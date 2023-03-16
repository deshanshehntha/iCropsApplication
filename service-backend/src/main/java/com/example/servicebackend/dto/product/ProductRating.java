package com.example.servicebackend.dto.product;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class ProductRating {

    @Id
    private String productRatingId;
    private String userId;
    private int rating;
    private String review;

}
