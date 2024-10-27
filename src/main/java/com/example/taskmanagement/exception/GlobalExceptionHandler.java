package com.example.taskmanagement.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.openapitools.model.ErrorResponse;
import org.openapitools.model.ErrorResponseErrorsInner;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Order(1)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(DateTimeParseException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Invalid date format");

        List<ErrorResponseErrorsInner> errors = new ArrayList<>();
        ErrorResponseErrorsInner errorDetail = new ErrorResponseErrorsInner();
        errorDetail.setField("dueDate");
        errorDetail.setMessage(ex.getParsedString() + " is not a valid date. Please use the format: yyyy-MM-dd");
        errors.add(errorDetail);

        errorResponse.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Validation failed");

        List<ErrorResponseErrorsInner> validationErrors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            ErrorResponseErrorsInner errorDetail = new ErrorResponseErrorsInner();
            errorDetail.setField(violation.getPropertyPath().toString());
            errorDetail.setMessage(violation.getMessage());
            validationErrors.add(errorDetail);
        }

        errorResponse.setErrors(validationErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Validation failed");

        List<ErrorResponseErrorsInner> validationErrors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            ErrorResponseErrorsInner errorDetail = new ErrorResponseErrorsInner();
            errorDetail.setField(error.getField());
            errorDetail.setMessage(error.getDefaultMessage());
            validationErrors.add(errorDetail);
        }

        errorResponse.setErrors(validationErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Invalid parameter type");

        List<ErrorResponseErrorsInner> errors = new ArrayList<>();
        ErrorResponseErrorsInner errorDetail = new ErrorResponseErrorsInner();
        errorDetail.setField(ex.getName());
        errorDetail.setMessage("Expected type: " + (ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"));
        errors.add(errorDetail);

        errorResponse.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
