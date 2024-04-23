package com.example.ecommerce.dto;

import com.example.ecommerce.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class CartResponseDto {
    private Integer id;
    private List<CartItem> cartItems;
}
