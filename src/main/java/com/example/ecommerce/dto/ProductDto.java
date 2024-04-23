package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDto {
    @NotBlank
    private String title;
    @NotNull
    private Double price;
    @NotBlank
    private String color;
    @NotBlank
    private String productType;
    @NotBlank
    private String brand;
    @NotBlank
    private String description;

    private List<ProductImageDto> productImages;
}
