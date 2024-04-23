package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.exception.CustomException;
import com.example.ecommerce.security.UserPrincipal;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.PaymentService;
import com.example.ecommerce.service.UserService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    PaymentService paymentService;

    @GetMapping("all")
    public ResponseEntity<List<Order>> getOrder(){
        UserPrincipal principal = userService.getUserPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getUserOrders(principal.getId()));
    }

    @GetMapping("single/{id}")
    public ResponseEntity<Order> getSingleOrder(@PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getUserSingleOrder(id));
    }

    @PostMapping("place")
    @ResponseStatus(HttpStatus.OK)
    public String charge(@RequestBody OrderDto orderDto) throws StripeException {
        if(Objects.equals(orderDto.getPaymentMode(), "online"))
            return paymentService.makePaymentOnline(orderDto);
        else if(Objects.equals(orderDto.getPaymentMode(), "offline")){
            paymentService.makePaymentOffline(orderDto);
            return "Ordered Placed";
        }
        else throw new CustomException(HttpStatus.BAD_REQUEST, "Payment mode not selected");
    }
}
