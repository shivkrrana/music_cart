package com.example.ecommerce.service;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.CustomException;
import com.example.ecommerce.mapper.OrderMapper;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    public List<Order> getUserOrders(Integer userId){
        return orderRepository.findByUserId(userId);
    }

    public Order getUserSingleOrder(Integer orderId){
        return orderRepository.findById(orderId).orElseThrow(()->
                new CustomException(HttpStatus.NOT_FOUND, "Invoice Not Found"));
    }

    public void createOrderByCartId(Integer cartId, String userName, String address, String paymentMode){
        Cart cart = cartService.getUserCartById(cartId);
        Order order = OrderMapper.toOrderByCart(cart, userName, address, paymentMode);
        orderRepository.save(order);
    }

    public void createOrderByProductId(Integer productId, Integer userId, String address, String paymentMode){
        Product product = productService.getProduct(productId);
        User user = userService.getUser(userId);
        Order order = OrderMapper.toOrderByProduct(product, user, address, paymentMode);
        orderRepository.save(order);
    }
}
