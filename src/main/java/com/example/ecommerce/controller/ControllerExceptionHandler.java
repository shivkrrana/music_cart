package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ErrorMsg;
import com.example.ecommerce.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorMsg> handleCustomException(CustomException ex) {
        log.error(ex.getMessage(), ex);
        ErrorMsg errorMsg = new ErrorMsg();
        errorMsg.setErrorMsg(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorMsg);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMsg> handleRuntimeException(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        ErrorMsg errorMsg = new ErrorMsg();
        errorMsg.setErrorMsg(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
    }

}
