package com.billingcontext.infrastructure.controller.exception;



import com.billingcontext.domain.shared.DomainException;
import com.billingcontext.infrastructure.gateway.stripe.GatewayConnectionException;
import com.billingcontext.infrastructure.persistence.exception.DatabaseOperationException;
import com.billingcontext.infrastructure.persistence.exception.DuplicateResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({DomainException.class})
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed for the requested data.");

        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }
    @ExceptionHandler(GatewayConnectionException.class)
    public ResponseEntity<ErrorResponse> handleGatewayException(GatewayConnectionException ex) {

        System.err.println("PAYMENT GATEWAY ERROR: " + ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_GATEWAY,
                "Unable to communicate with the payment provider. Please try again later.");
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(com.billingcontext.infrastructure.persistence.exception.DuplicateResourceException ex) {
       return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseOperationException(com.billingcontext.infrastructure.persistence.exception.DatabaseOperationException ex) {

        System.err.println("DATABASE ERROR: " + ex.getMessage());
        ex.printStackTrace();

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "A system error occurred while accessing data.");
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        System.err.println("UNEXPECTED ERROR: " + ex.getMessage());
        ex.printStackTrace();

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected system error occurred.");
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(status.value(), message, LocalDateTime.now());
        return new ResponseEntity<>(error, status);
    }

    public record ErrorResponse(int status, String message, LocalDateTime timestamp) {}
}