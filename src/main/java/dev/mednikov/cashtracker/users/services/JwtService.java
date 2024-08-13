package dev.mednikov.cashtracker.users.services;

public interface JwtService {

    String generateToken(String username);

    String getUsernameFromToken(String token);

}
