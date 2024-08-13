package dev.mednikov.cashtracker.users.dto.mappers;

import dev.mednikov.cashtracker.users.dto.UserDto;
import dev.mednikov.cashtracker.users.models.User;

import java.util.function.Function;

public final class UserDtoMapper implements Function<User, UserDto> {
    @Override
    public UserDto apply(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
