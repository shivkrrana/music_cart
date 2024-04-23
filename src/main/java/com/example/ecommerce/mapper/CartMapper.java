package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.CartDto;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Product;

public interface CartMapper {
    static CartItem toCartItem(CartDto cartDto, Product product, Cart cart){
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(cartDto.getQuantity());
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cart.addItem(cartItem);
        return cartItem;
    }
}
