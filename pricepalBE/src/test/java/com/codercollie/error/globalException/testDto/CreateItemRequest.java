package com.codercollie.error.globalException.testDto;

import jakarta.validation.constraints.NotBlank;

public class CreateItemRequest {

    @NotBlank(message = "must be not blank")
    public String name;
}
