package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private int cartId;
    private int productId;
    private String paymentMode;
    private String userName;
    private String address;
}
