package com.codercollie.error.globalException.testDto;

import jakarta.validation.constraints.NotBlank;

public class CreateProductRequest {

    @NotBlank(message = "must be not blank")
    public String name;
}
