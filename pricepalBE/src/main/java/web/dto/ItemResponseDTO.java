package web.dto;

import java.math.BigDecimal;

public record ItemResponseDTO(
        Long id,
        String name,
        BigDecimal price,
        String supermarket,
        String notes
) {}


