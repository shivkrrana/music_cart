package com.example.ecommerce.aop;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.security.UserPrincipal;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
public class CreateCartAspect {

    private final UserService userService;
    private final CartService cartService;

    @Around("@annotation(com.example.ecommerce.aop.CreateCart)")
    public Object jointPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authenticationToken.getPrincipal();
        Cart cart = cartService.getUserCart(userPrincipal.getId());
        if (Objects.isNull(cart)) {
            User user = userService.getUser(userPrincipal.getId());
            cartService.createUserCart(user);
        }
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof UserPrincipal) {
                args[i] = userPrincipal;
                break;
            }
        }
        return joinPoint.proceed(args);
    }

}
