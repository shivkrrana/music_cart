package com.example.ecommerce.exception;

import com.example.ecommerce.dto.ErrorMsg;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationException {
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMsg> validationExceptionHandler(MethodArgumentNotValidException ex){
        ErrorMsg msg = new ErrorMsg();
        msg.setErrorMsg("Input validation failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }
}
