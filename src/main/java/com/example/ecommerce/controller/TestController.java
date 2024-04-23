package com.example.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class TestController {

    public ResponseEntity<String> test(){
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Successfully");
    }
}
