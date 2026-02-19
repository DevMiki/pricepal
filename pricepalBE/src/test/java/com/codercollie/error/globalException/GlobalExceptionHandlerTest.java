package com.codercollie.error.globalException;

import com.codercollie.error.ApiError;
import com.codercollie.error.GlobalExceptionHandler;
import com.codercollie.error.ItemNotFoundException;
import com.codercollie.error.ValidationApiError;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    @Test
    void handleGeneric_returns500AndApiErrorBody(){
        final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        final Exception exception = new Exception("Ka-boom!");
        mockHttpServletRequest.setRequestURI("/api/test");

        final ResponseEntity<ApiError> response = globalExceptionHandler.handleGeneric(exception, mockHttpServletRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        final ApiError body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.timestamp());
        assertEquals(500, body.status());
        assertEquals("Internal Server Error", body.error());
        assertEquals("Ka-boom!", body.message());
        assertEquals("/api/test", body.path());
    }

    @Test
    void handleItemNotFound_returns404WithApiErrorBody(){
        final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        final ItemNotFoundException itemNotFoundException = new ItemNotFoundException(42L);
        mockHttpServletRequest.setRequestURI("/api/test");

        final ResponseEntity<ApiError> response = globalExceptionHandler.handleItemNotFound(itemNotFoundException, mockHttpServletRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        final ApiError body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.timestamp());
        assertEquals(404, body.status());
        assertEquals("Not Found", body.error());
        assertTrue(body.message().contains("Item not found") && body.message().contains("42"),
                "Expected message to contain both 'Item not found' and '42' but was: " + body.message());
        assertEquals("/api/test", body.path());
    }

    @Test
    void handleValidation_returns400WithApiErrorBody(){
        final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setRequestURI("/api/items");

        final BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "createProductRequest");
        bindingResult.addError(new FieldError("createProductRequest", "kiwi", "must not be blank!"));
        final MethodArgumentNotValidException argumentNotValidException = mockValidationException(bindingResult);

        final ResponseEntity<ValidationApiError> response = globalExceptionHandler.handleValidation(argumentNotValidException, mockHttpServletRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        final ValidationApiError body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.timestamp());
        assertEquals(400, body.status());
        assertEquals("Bad Request", body.error());
        assertEquals("Validation failed", body.message());
        assertEquals("/api/items", body.path());
        assertEquals(Map.of("kiwi", List.of("must not be blank!")), body.fieldErrors());
    }

    @Test
    void handleValidation_groupsFieldErrorsByFieldName(){
        final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setRequestURI("/api/items");

        final BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "createProductRequest");
        bindingResult.addError(new FieldError("createProductRequest", "kiwi", "must not be blank!"));
        bindingResult.addError(new FieldError("createProductRequest", "kiwi", "size must be between 2 and 42"));
        bindingResult.addError(new FieldError("createProductRequest", "price", "must be greater than 0"));

        final MethodArgumentNotValidException argumentNotValidException = mockValidationException(bindingResult);

        final ResponseEntity<ValidationApiError> response = globalExceptionHandler.handleValidation(argumentNotValidException, mockHttpServletRequest);
        final ValidationApiError body = response.getBody();
        assertNotNull(body);

        final Map<String, List<String>> fieldErrors = body.fieldErrors();
        assertNotNull(fieldErrors);
        assertEquals(2, fieldErrors.size());
        assertEquals(List.of("must not be blank!", "size must be between 2 and 42"), fieldErrors.get("kiwi"));
        assertEquals(List.of("must be greater than 0"), fieldErrors.get("price"));
    }

    @Test
    void handleValidation_returnsEmptyFieldErrors_whenValidationHasNoFieldErrors(){
        final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setRequestURI("/api/items");

        final BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "createProductRequest");
        final MethodArgumentNotValidException argumentNotValidException = mockValidationException(bindingResult);

        final ResponseEntity<ValidationApiError> response = globalExceptionHandler.handleValidation(argumentNotValidException, mockHttpServletRequest);
        final ValidationApiError body = response.getBody();
        assertNotNull(body);

        final Map<String, List<String>> fieldErrors = body.fieldErrors();
        assertNotNull(fieldErrors);
        assertTrue(fieldErrors.isEmpty());
    }

    private static MethodArgumentNotValidException mockValidationException(BeanPropertyBindingResult bindingResult) {
        final MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        return methodArgumentNotValidException;
    }
}
