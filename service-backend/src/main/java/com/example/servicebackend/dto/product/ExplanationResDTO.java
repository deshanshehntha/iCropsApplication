package com.example.servicebackend.dto.product;

import lombok.Data;

@Data
public class ExplanationResDTO {
    private String comparedText;
    private String positiveSummary;
    private String negativeSummary;
}
