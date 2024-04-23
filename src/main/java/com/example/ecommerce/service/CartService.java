package com.example.ecommerce.service;

import com.example.ecommerce.dto.CartDto;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.exception.CustomException;
import com.example.ecommerce.mapper.CartMapper;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Objects;

@Service
public class CartService {

    @Autowired
    private ProductService productService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    public void createUserCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    public void addItemToCart(Integer userId, CartDto cartDto) {
        Cart cart = getUserCart(userId);
        Product product = productService.getProduct(cartDto.getProductId());

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);
        if(cartItem != null){
            int newQuantity = cartDto.getQuantity();
                cartItem.setQuantity(newQuantity + cartItem.getQuantity());
        }
        else {
            cartItem = CartMapper.toCartItem(cartDto, product, cart);
        }
        cartItemRepository.save(cartItem);
    }

    public Cart getUserCart(Integer userId){
        return cartRepository.findByUserId(userId).orElse(null);
    }

    public Cart getUserCartById(Integer cartId){
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null)
            throw new CustomException(HttpStatus.BAD_REQUEST, "Cart not exists");
        return cart;
    }

    public int getUserCartQuantity(Integer userId){
        Cart cart = getUserCart(userId);
        if(cart == null){
            return 0;
        }

        int count = 0;
        for(CartItem cartItem : cart.getCartItems()){
            count += cartItem.getQuantity();
        }

        return count;
    }

    public void deleteCart(Integer cartId){
        Cart cart = getUserCartById(cartId);
        cartRepository.delete(cart);
    }

    public void deleteCartItem(Integer cartItemId){
        cartItemRepository.deleteById(cartItemId);
    }
}
