package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartDto {
    private int productId;
    private int quantity;
}
