package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterProductDto {
    private String color;
    private String brand;
    private String search;
    private String productType;
    private Double minPrice = -1d;
    private Double maxPrice = -1d;
    private String sort;
}
