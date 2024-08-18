package dev.mednikov.cashtracker.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDto(
        Long id,
        Long ownerId,
        @NotNull @NotBlank String name,
        String description,
        @NotNull @NotBlank String color
) {
}
