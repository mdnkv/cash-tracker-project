package dev.mednikov.cashtracker.accounts.dto;

import dev.mednikov.cashtracker.accounts.models.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentAccountDto(
        Long id,
        Long ownerId,
        @NotNull @NotBlank String name,
        String description,
        @NotNull AccountType accountType
) {
}
