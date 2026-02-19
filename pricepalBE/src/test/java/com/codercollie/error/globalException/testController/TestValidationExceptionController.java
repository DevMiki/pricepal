package com.codercollie.error.globalException.testController;

import com.codercollie.error.globalException.testDto.CreateProductRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/product-test")
@RestController
public class TestValidationExceptionController {

    @PostMapping
    void createProduct(@Valid @RequestBody CreateProductRequest request){}
}
