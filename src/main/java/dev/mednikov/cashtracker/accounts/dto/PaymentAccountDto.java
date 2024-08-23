package dev.mednikov.cashtracker.accounts.dto;

import dev.mednikov.cashtracker.accounts.models.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PaymentAccountDto(
        Long id,
        Long ownerId,
        @NotNull @NotBlank @Size(max=250) String name,
        @Size(max=250) String description,
        @NotNull AccountType accountType
) {
}
