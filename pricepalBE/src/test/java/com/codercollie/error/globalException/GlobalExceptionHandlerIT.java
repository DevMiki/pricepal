package com.codercollie.error.globalException;

import com.codercollie.error.GlobalExceptionHandler;
import com.codercollie.error.globalException.testController.TestExceptionController;
import com.codercollie.error.globalException.testController.TestValidationExceptionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = { TestExceptionController.class, TestValidationExceptionController.class})
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void kaBoom_returns500_withApiErrorBody() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/test"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Ka-boom!"))
                .andExpect(jsonPath("$.path").value("/api/test"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void validationError_returns400_andBody() throws Exception {
        mockMvc.perform(post("/api/product-test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.path").value("/api/product-test"))
                .andExpect(jsonPath("$.fieldErrors.name[0]").value("must be not blank"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
