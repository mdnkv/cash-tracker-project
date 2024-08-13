package dev.mednikov.cashtracker.users.dto;

public record LoginResponseDto(
        String token,
        Long id
) {
}
