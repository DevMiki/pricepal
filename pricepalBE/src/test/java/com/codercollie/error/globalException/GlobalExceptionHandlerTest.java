package com.codercollie.error.globalException;

import com.codercollie.error.ApiError;
import com.codercollie.error.GlobalExceptionHandler;
import com.codercollie.error.ItemNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

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
}
