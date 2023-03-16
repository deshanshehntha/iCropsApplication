package com.example.servicebackend.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductReaderDTO {

    private String id;
    private String category;
    private String tech1;
    private String description;
    private String fit;
    private String title;
    private String also_buy;
    private String tech2;
    private String brand;
    private String feature;
    private String rank;
    private String also_view;
    private String main_cat;
    private String similar_item;
    private String date;
    private String price;
    private String asin;
    private String imageURL;
    private String imageURLHighRes;
    private String details;
    private String category_in_string;
}
