package com.example.ecommerce.controller;

import com.example.ecommerce.aop.CreateCart;
import com.example.ecommerce.dto.CartCountDto;
import com.example.ecommerce.dto.CartDto;
import com.example.ecommerce.dto.CartResponseDto;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.security.UserPrincipal;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    UserService userService;

    @CreateCart
    @PostMapping("add")
    public ResponseEntity<CartCountDto> addToCart(@RequestBody CartDto cartDto, UserPrincipal principal){
        cartService.addItemToCart(principal.getId(), cartDto);
        int count = cartService.getUserCartQuantity(principal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CartCountDto(count));
    }

    @GetMapping("all")
    public ResponseEntity<CartResponseDto> getCart(){
        UserPrincipal principal = userService.getUserPrincipal();
        Cart cart = cartService.getUserCart(principal.getId());
        CartResponseDto cartResponseDto = new CartResponseDto();
        cartResponseDto.setCartItems(cart.getCartItems());
        cartResponseDto.setId(cart.getId());
        return ResponseEntity.status(HttpStatus.OK).body(cartResponseDto);
    }

    @GetMapping("count")
    public ResponseEntity<CartCountDto> getCartCount() {
        UserPrincipal principal = userService.getUserPrincipal();
        int count = cartService.getUserCartQuantity(principal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new CartCountDto(count));
    }

    @GetMapping("remove/{cartItemId}")
    public ResponseEntity<CartCountDto> removeCartItem(@PathVariable int cartItemId) {
        cartService.deleteCartItem(cartItemId);
        UserPrincipal principal = userService.getUserPrincipal();
        int count = cartService.getUserCartQuantity(principal.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new CartCountDto(count));
    }
}
