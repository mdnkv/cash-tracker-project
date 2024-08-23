package dev.mednikov.cashtracker.categories.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryDto(
        Long id,
        Long ownerId,
        @NotNull @NotBlank @Size(max = 250) String name,
        @Size(max=250) String description,
        @NotNull @NotBlank String color
) {
}
