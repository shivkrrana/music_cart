package com.example.ecommerce.controller;

import com.example.ecommerce.dto.UserDto;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.security.ResponseToken;
import com.example.ecommerce.security.UserPrincipal;
import com.example.ecommerce.service.UserService;
import com.nimbusds.jose.KeyLengthException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("signup")
    public ResponseEntity<ResponseToken> signUp(@RequestBody @Valid UserDto userDto) throws KeyLengthException {
        ResponseToken token = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("login")
    public ResponseEntity<ResponseToken> logIn(@RequestBody UserDto userDto) throws KeyLengthException {
        ResponseToken token = userService.loginUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping("test")
    public String test(){
        return "Hello From Test";
    }

    @GetMapping("auth")
    @ResponseStatus(HttpStatus.OK)
    public void isAuth(){
    }

    @GetMapping("name")
    @ResponseStatus(HttpStatus.OK)
    public String getName(){
        return userService.getUserName();
    }
}
