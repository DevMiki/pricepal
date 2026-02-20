package web.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ItemUpdateDTO(
        @NotBlank String name,
        @NotNull @Digits(integer = 8, fraction = 2) @PositiveOrZero BigDecimal price,
        String supermarket,
        String notes
        ) {}
