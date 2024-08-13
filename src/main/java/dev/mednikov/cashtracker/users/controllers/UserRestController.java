package dev.mednikov.cashtracker.users.controllers;

import dev.mednikov.cashtracker.users.dto.CreateUserDto;
import dev.mednikov.cashtracker.users.dto.LoginRequestDto;
import dev.mednikov.cashtracker.users.dto.LoginResponseDto;
import dev.mednikov.cashtracker.users.dto.UserDto;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.services.UserService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody UserDto createUser (@RequestBody @Valid CreateUserDto body){
        return userService.createUser(body);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody LoginResponseDto login (@RequestBody @Valid LoginRequestDto body){
        return userService.loginWithEmailAndPassword(body);
    }

    @GetMapping("/current-user")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UserDto getCurrentUser(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return userService.getCurrentUser(user.getId());
    }

    @PutMapping("/current-user")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UserDto updateCurrentUser(Authentication auth, @RequestBody @Valid UserDto body){
        User user = (User) auth.getPrincipal();
        Long userId = user.getId();
        UserDto updateRequest = new UserDto(userId, body.name(), body.email());
        return userService.updateCurrentUser(updateRequest);
    }

    @DeleteMapping("/delete-user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentUser(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        userService.deleteCurrentUser(user.getId());
    }

}
