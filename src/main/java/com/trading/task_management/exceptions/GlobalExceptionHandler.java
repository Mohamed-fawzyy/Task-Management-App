package com.trading.task_management.exceptions;

import com.trading.task_management.util.dto.CustomApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.security.core.AuthenticationException;
import io.jsonwebtoken.JwtException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomApiResponse<Map<String, String>>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("input", "'" + ex.getValue() + "' is not a valid " + ex.getRequiredType().getSimpleName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomApiResponse.of(400, "Invalid parameter type", error));
    }

    // Handle validation errors for @Min, @Max, etc.
    // Handles @Valid on method parameters (@RequestParam, @PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomApiResponse<Map<String, String>>> handleConstraintViolation(
            ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String path = violation.getPropertyPath().toString();
            errors.put(path, violation.getMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomApiResponse.of(400, "Invalid parameters", errors));
    }

    // Handles @Valid on @RequestBody DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomApiResponse.of(400, "Validation failed", errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String rootMsg = ex.getMostSpecificCause().getMessage();
        String message;

        if (rootMsg.contains("Required request body is missing")) {
            message = "Request body is required";
        }
        else if (rootMsg.contains("JSON parse error")) {
            message = "Invalid JSON format: " + rootMsg;
        }
        else {
            message = "Invalid request: " + rootMsg;
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomApiResponse.of(400, message));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleNullPointerException(NullPointerException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomApiResponse.of(400, "A required field was missing or invalid"));
    }

    @ExceptionHandler(EmailSendFailureException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleEmailSendFailure(EmailSendFailureException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CustomApiResponse.of(500, ex.getMessage()));
    }

    @ExceptionHandler(ResourceBadRequestException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleRequestValidationException(ResourceBadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomApiResponse.of(400, ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CustomApiResponse.of(500, "Unexpected server error: " + ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CustomApiResponse.of(401, "Authentication failed: " + ex.getMessage()));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleJwtException(JwtException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CustomApiResponse.of(401, "Invalid token: " + ex.getMessage()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(CustomApiResponse.of(409, ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleNotFoundResourceException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CustomApiResponse.of(404, ex.getMessage()));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleInternalServerErrorException(InternalServerErrorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CustomApiResponse.of(500, ex.getMessage()));
    }

    @ExceptionHandler(ResourceForbiddenException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleResourceForbiddenException(ResourceForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(CustomApiResponse.of(403, ex.getMessage()));
    }

    @ExceptionHandler(ResourceFoundWithNoContent.class)
    public ResponseEntity<CustomApiResponse<Void>> handleResourceFoundWithNoContent(ResourceFoundWithNoContent ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(CustomApiResponse.of(204, ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotProcessableException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleResourceNotProcessableException(ResourceNotProcessableException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(CustomApiResponse.of(422, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomApiResponse.of(400, ex.getMessage()));
    }


    // Handle invalid content type for request body
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(CustomApiResponse.of(415,
                        "Unsupported media type: " + ex.getContentType()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<CustomApiResponse<Void>> handleMissingParams(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CustomApiResponse.of(
                        400,
                        "Missing required parameter: " + ex.getParameterName()
                ));
    }
}
