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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiError> handleItemNotFound(
            ItemNotFoundException itemNotFoundException,
            HttpServletRequest httpServletRequest
    ) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        final ApiError errorBody = new ApiError(
                Instant.now(),
                notFound.value(),
                notFound.getReasonPhrase(),
                itemNotFoundException.getMessage(),
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.status(notFound).body(errorBody);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpServletRequest httpServletRequest
    ) {

        final Map<String, List<String>> fieldErrors = methodArgumentNotValidException.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        final LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", badRequest.value());
        body.put("error", badRequest.getReasonPhrase());
        body.put("message", "Validation failed");
        body.put("path", httpServletRequest.getRequestURI());
        body.put("fieldErrors", fieldErrors);

        return ResponseEntity.status(badRequest).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception exception,
            HttpServletRequest httpServletRequest
    ) {
        final HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        final ApiError body = new ApiError(
                Instant.now(),
                internalServerError.value(),
                internalServerError.getReasonPhrase(),
                exception.getMessage(),
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.status(internalServerError).body(body);
    }
}
