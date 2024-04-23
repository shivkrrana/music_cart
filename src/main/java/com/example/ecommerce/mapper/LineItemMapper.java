package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.CustomException;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface LineItemMapper {

    static void toProductLineItem(Product product, List<SessionCreateParams.LineItem> allElements){
        allElements.add(
                SessionCreateParams.LineItem.builder().setQuantity((long) 1).setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("inr")
                                .setUnitAmount(product.getPrice().longValue() * 100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(product.getTitle())
                                        .addImage(product.getImage())
                                        .build()
                                )
                                .build()
                ).build()
        );
    }

    static void toCartLineItem(Cart cart, List<SessionCreateParams.LineItem> allElements){
        if(cart == null)
            throw new CustomException(HttpStatus.BAD_REQUEST, "Cart not found");

        for(CartItem cartItem : cart.getCartItems()){
            allElements.add(
                    SessionCreateParams.LineItem.builder().setQuantity((long) cartItem.getQuantity()).setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("inr")
                                    .setUnitAmount(cartItem.getProduct().getPrice().longValue() * 100)
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName(cartItem.getProduct().getTitle())
                                            .addImage(cartItem.getProduct().getImage())
                                            .build()
                                    )
                                    .build()
                    ).build()
            );
        }
    }
}
