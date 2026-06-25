package com.bank.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bank.account.dto.ApiErrorResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String description = "Validation failed";
        if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
            FieldError fe = ex.getBindingResult().getFieldErrors().get(0);
            description = fe.getDefaultMessage();
        }
        return ResponseEntity.badRequest().body(
                ApiErrorResponse.builder()
                        .transactionStatusCode(HttpStatus.BAD_REQUEST.value())
                        .transactionStatusDescription(description)
                        .build()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
        String description = "Malformed request body";
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();
            description = ife.getMessage().split("\n")[0];
        }
        return ResponseEntity.badRequest().body(
                ApiErrorResponse.builder()
                        .transactionStatusCode(HttpStatus.BAD_REQUEST.value())
                        .transactionStatusDescription(description)
                        .build()
        );
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicate(AccountAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                ApiErrorResponse.builder()
                        .transactionStatusCode(HttpStatus.CONFLICT.value())
                        .transactionStatusDescription(ex.getMessage())
                        .build()
        );
    }

    
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ApiErrorResponse.builder()
                        .transactionStatusCode(401)
                        .transactionStatusDescription(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiErrorResponse.builder()
                        .transactionStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .transactionStatusDescription("An unexpected error occurred. Please try again later.")
                        .build()
        );
    }
}
