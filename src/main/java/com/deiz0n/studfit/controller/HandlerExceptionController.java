package com.deiz0n.studfit.controller;

import com.deiz0n.studfit.domain.exceptions.ResourceNotFoundException;
import com.deiz0n.studfit.domain.response.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class HandlerExceptionController  {

    @ExceptionHandler
    public ResponseEntity<ResponseError> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ResponseError.builder()
                                .code(HttpStatus.NOT_FOUND.value())
                                .title("Resource not found")
                                .status(HttpStatus.NOT_FOUND)
                                .description(exception.getMessage())
                                .build()
                );
    }


}
