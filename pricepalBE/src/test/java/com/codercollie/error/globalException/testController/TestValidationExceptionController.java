package com.codercollie.error.globalException.testController;

import com.codercollie.error.globalException.testDto.CreateItemRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/item-test")
@RestController
public class TestValidationExceptionController {

    @PostMapping
    void createItem(@Valid @RequestBody CreateItemRequest request){}
}
