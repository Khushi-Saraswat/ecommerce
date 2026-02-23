package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//makes this class a global exception handler for all controller
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BaseException ex) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (ex.getErrorCode().startsWith("AUTH"))
            status = HttpStatus.UNAUTHORIZED;
        if (ex.getErrorCode().startsWith("USER"))
            status = HttpStatus.NOT_FOUND;
        if (ex.getErrorCode().startsWith("ORDER"))
            status = HttpStatus.BAD_REQUEST;
        if (ex.getErrorCode().startsWith("ADMIN"))
            status = HttpStatus.FORBIDDEN;

        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                ex.getErrorCode(),
                status.value());

        return new ResponseEntity<>(response, status);

    }

}