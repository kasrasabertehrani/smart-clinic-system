package com.smartclinicsystem.infrastructure.adapters.in.web.exception;

import com.smartclinicsystem.application.exception.ConcurrentOperationException;
import com.smartclinicsystem.domain.exception.AppointmentException;
import com.smartclinicsystem.domain.exception.DomainException;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.exception.DatabaseOperationException;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.exception.DuplicateResourceException;
import com.smartclinicsystem.domain.exception.InvalidEffectiveScheduleException;

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


    @ExceptionHandler({DuplicateResourceException.class, ConcurrentOperationException.class})
    public ResponseEntity<ErrorResponse> handleConflictExceptions(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }


    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseOperationException(DatabaseOperationException ex) {
        System.err.println("CRITICAL DB ERROR: " + ex.getCause().getMessage());


        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "The system is currently experiencing technical difficulties processing your data. Please try again later.");
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