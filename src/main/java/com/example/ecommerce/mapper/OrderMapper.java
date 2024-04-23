package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.*;

import java.util.ArrayList;
import java.util.List;

public interface OrderMapper {
    static Order toOrderByCart(Cart cart, String userName, String address, String paymentMode){
        double totalPrice = 0.0;
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setUserName(userName);
        order.setAddress(address);
        order.setPaymentMode(paymentMode);
        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            totalPrice += cartItem.getProduct().getPrice() * cartItem.getQuantity();
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalPrice);
        return order;
    }

    static Order toOrderByProduct(Product product, User user, String address, String paymentMode){
        Order order = new Order();
        order.setUser(user);
        order.setUserName(user.getName());
        order.setAddress(address);
        order.setPaymentMode(paymentMode);
        order.setTotalAmount(product.getPrice());
        List<OrderItem> orderItems = new ArrayList<>();
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(product.getPrice());
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(1);
            orderItems.add(orderItem);

        order.setOrderItems(orderItems);
        return order;
    }
}
