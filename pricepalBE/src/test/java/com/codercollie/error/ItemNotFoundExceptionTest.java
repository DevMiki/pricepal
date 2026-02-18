package com.codercollie.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ItemNotFoundExceptionTest {

    @Test
    void constructor_setsExpectedMessage(){
        final ItemNotFoundException ex = assertThrows(
                ItemNotFoundException.class,
                () -> {
                    throw new ItemNotFoundException(42L);
                }
        );
        assertEquals("Item not found: 42", ex.getMessage());
    }
}
