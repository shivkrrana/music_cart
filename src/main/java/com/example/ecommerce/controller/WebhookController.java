package com.example.ecommerce.controller;

import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class WebhookController {
    @Value("${stripe.webhookKey}")
    private String webhookSecret;

    @Autowired
    private CartService cartService;

    @Autowired
    OrderService orderService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhookEvent(@RequestBody String payload, HttpServletRequest request) {
        // Verify webhook signature
        String signatureHeader = request.getHeader("Stripe-Signature");
        try {
            Event event = Webhook.constructEvent(payload, signatureHeader, webhookSecret);

            // Handle the event
            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                if(session!=null) {
                    Map<String, String> map = session.getMetadata();
                    int productId = Integer.parseInt(map.get("productId"));
                    int cartId = Integer.parseInt(map.get("cartId"));
                    int userId = Integer.parseInt(map.get("userId"));
                    String userName = map.get("userName");
                    String address = map.get("address");

                    if(cartId != 0){
                        orderService.createOrderByCartId(cartId, userName, address, "online");
                        cartService.deleteCart(cartId);
                    }
                    else if(productId != 0){
                        orderService.createOrderByProductId(productId, userId, address, "online");
                    }

                    System.out.println("MetaData " + session);
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook Error");
        }
    }
}
