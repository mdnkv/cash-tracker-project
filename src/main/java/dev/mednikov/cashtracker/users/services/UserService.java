package dev.mednikov.cashtracker.users.services;

import dev.mednikov.cashtracker.users.dto.CreateUserDto;
import dev.mednikov.cashtracker.users.dto.LoginRequestDto;
import dev.mednikov.cashtracker.users.dto.LoginResponseDto;
import dev.mednikov.cashtracker.users.dto.UserDto;

public interface UserService {

    UserDto createUser (CreateUserDto requestDto);

    LoginResponseDto loginWithEmailAndPassword(LoginRequestDto requestDto);

    UserDto getCurrentUser (Long userId);

    UserDto updateCurrentUser (UserDto userDto);

    void deleteCurrentUser (Long userId);
}
