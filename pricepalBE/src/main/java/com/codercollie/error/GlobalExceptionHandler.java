package com.codercollie.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiError> handleItemNotFound(
            ItemNotFoundException itemNotFoundException,
            HttpServletRequest httpServletRequest
    ) {
        final HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        final ApiError errorBody = new ApiError(
                Instant.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                itemNotFoundException.getMessage(),
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(errorBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpServletRequest httpServletRequest
    ) {
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        final LinkedHashMap<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            String field = error.getField();
            fieldErrors.put(field, error.getDefaultMessage());
        }

        final LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", httpStatus);
        body.put("error", httpStatus.getReasonPhrase());
        body.put("message", "Validation failed");
        body.put("path", httpServletRequest.getRequestURI());
        body.put("fieldErrors", fieldErrors);

        return ResponseEntity.status(httpStatus).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception exception,
            HttpServletRequest httpServletRequest
    ) {
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final ApiError body = new ApiError(
                Instant.now(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                "Unexpected error. Please try again later.",
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(body);
    }
}
