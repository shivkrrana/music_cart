package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.CustomException;
import com.example.ecommerce.mapper.LineItemMapper;
import com.example.ecommerce.security.UserPrincipal;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    public String makePaymentOnline(OrderDto orderDto) throws StripeException {
        Stripe.apiKey = secretKey;
        UserPrincipal userPrincipal = userService.getUserPrincipal();

        List<SessionCreateParams.LineItem> allElements = new ArrayList<>();

            if(orderDto.getCartId() == 0 && orderDto.getProductId() == 0){
                throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid Input");
            }
            else if(orderDto.getCartId() != 0){
                Cart cart = cartService.getUserCartById(orderDto.getCartId());
                LineItemMapper.toCartLineItem(cart, allElements);
            }
            else {
                Product product = productService.getProduct(orderDto.getProductId());
                LineItemMapper.toProductLineItem(product, allElements);
            }


            SessionCreateParams params = SessionCreateParams.builder().addPaymentMethodType(
                SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCustomerEmail(userPrincipal.getUsername())
                    .putMetadata("cartId", String.valueOf(orderDto.getCartId()))
                    .putMetadata("productId", String.valueOf(orderDto.getProductId()))
                    .putMetadata("userId", String.valueOf(userPrincipal.getId()))
                    .putMetadata("userName", orderDto.getUserName())
                    .putMetadata("address", orderDto.getAddress())
//                    .addShippingOption(SessionCreateParams.ShippingOption.builder().setShippingRateData(SessionCreateParams.ShippingOption.ShippingRateData.builder().setDisplayName("Delivery charge").setFixedAmount(SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder().setAmount(45L).setCurrency("inr").putCurrencyOption("inr", SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.CurrencyOption.builder().setAmount(45L).build()).build()).build()).build())
//                    .setShippingAddressCollection(SessionCreateParams.ShippingAddressCollection.builder().addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.IN).build())
//                    .setCustomerCreation(SessionCreateParams.CustomerUpdate.builder().setAddress())
                    .addShippingOption(SessionCreateParams.ShippingOption.builder().setShippingRateData(SessionCreateParams.ShippingOption.ShippingRateData.builder().setDisplayName("You will receive your order anywhere from 5-7 business days").setFixedAmount(SessionCreateParams.ShippingOption.ShippingRateData.FixedAmount.builder().setAmount(45L * 100).setCurrency("inr").build()).setType(SessionCreateParams.ShippingOption.ShippingRateData.Type.FIXED_AMOUNT).build()).build())
                .setSuccessUrl("http://localhost:3000/success")
                .setCancelUrl("http://localhost:3000/checkout")
                .addAllLineItem(allElements).build();

        Session session = Session.create(params);
        return session.getId();
    }

    public void makePaymentOffline(OrderDto orderDto){
        UserPrincipal principal = userService.getUserPrincipal();
        if(orderDto.getCartId() != 0){
            orderService.createOrderByCartId(orderDto.getCartId(), orderDto.getUserName(), orderDto.getAddress(), orderDto.getPaymentMode());
            cartService.deleteCart(orderDto.getCartId());
        }
        else if(orderDto.getProductId() != 0){
            orderService.createOrderByProductId(orderDto.getProductId(), principal.getId(), orderDto.getAddress(), orderDto.getPaymentMode());
        }
    }
}
