package dev.mednikov.cashtracker.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserDto(
        @NotNull @NotBlank @Email String email,
        @NotNull @NotBlank String name,
        @NotNull @NotBlank String password
) {
}
