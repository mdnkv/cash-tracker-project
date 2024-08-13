package dev.mednikov.cashtracker.users.controllers;

import dev.mednikov.cashtracker.core.BaseIT;
import dev.mednikov.cashtracker.users.dto.CreateUserDto;
import dev.mednikov.cashtracker.users.dto.LoginRequestDto;
import dev.mednikov.cashtracker.users.dto.LoginResponseDto;
import dev.mednikov.cashtracker.users.dto.UserDto;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;
import dev.mednikov.cashtracker.users.services.JwtService;
import dev.mednikov.cashtracker.users.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

class UserRestControllerIT extends BaseIT {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void createUser_alreadyExistsTest() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        CreateUserDto payload = new CreateUserDto(email, name, password);

        userService.createUser(payload);

        HttpEntity<CreateUserDto> httpEntity = new HttpEntity<>(payload);
        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "/api/users/create-user", HttpMethod.POST, httpEntity, UserDto.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void createUser_successTest(){
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        CreateUserDto payload = new CreateUserDto(email, name, password);

        // prepare request
        HttpEntity<CreateUserDto> httpEntity = new HttpEntity<>(payload);
        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "/api/users/create-user", HttpMethod.POST, httpEntity, UserDto.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // assert that user was created
        Optional<User> userResult = userRepository.findByEmail(email);
        Assertions.assertThat(userResult).isPresent();
    }

    @Test
    void login_badCredentialsTest(){
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String hash = passwordEncoder.encode(faker.internet().password());
        User user = User.UserBuilder.anUser()
                .withEmail(email)
                .withPassword(hash)
                .withName(faker.name().fullName())
                .build();
        User userResult = userRepository.save(user);

        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);
        HttpEntity<LoginRequestDto> httpEntity = new HttpEntity<>(loginRequestDto);
        ResponseEntity<LoginResponseDto> response = testRestTemplate.exchange(
                "/api/users/login", HttpMethod.POST, httpEntity, LoginResponseDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    void login_userDoesNotExistTest(){
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();

        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);
        HttpEntity<LoginRequestDto> httpEntity = new HttpEntity<>(loginRequestDto);
        ResponseEntity<LoginResponseDto> response = testRestTemplate.exchange(
                "/api/users/login", HttpMethod.POST, httpEntity, LoginResponseDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    void login_successTest(){
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String hash = passwordEncoder.encode(password);
        User user = User.UserBuilder.anUser()
                .withEmail(email)
                .withPassword(hash)
                .withName(faker.name().fullName())
                .build();
        User userResult = userRepository.save(user);

        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);
        HttpEntity<LoginRequestDto> httpEntity = new HttpEntity<>(loginRequestDto);
        ResponseEntity<LoginResponseDto> response = testRestTemplate.exchange(
                "/api/users/login", HttpMethod.POST, httpEntity, LoginResponseDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

    }


    @Test
    void getCurrentUser_notAuthenticatedTest(){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "/api/users/current-user", HttpMethod.GET, httpEntity, UserDto.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

    }


    @Test
    void getCurrentUser_successTest(){
        User user = User.UserBuilder.anUser()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withPassword(faker.internet().password())
                .build();
        User userResult = userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "/api/users/current-user", HttpMethod.GET, httpEntity, UserDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        UserDto body = response.getBody();
        Assertions.assertThat(body)
                .hasFieldOrPropertyWithValue("email", user.getEmail())
                .hasFieldOrPropertyWithValue("name", user.getName())
                .hasFieldOrPropertyWithValue("id", userResult.getId());

    }


    @Test
    void updateCurrentUser_notAuthenticatedTest(){
        Long userId = faker.random().nextLong();
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        UserDto userDto = new UserDto(userId, name, email);
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(userDto);
        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "/api/users/current-user", HttpMethod.PUT, httpEntity, UserDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    @Test
    void updateCurrentUser_emailAlreadyExistsTest(){
        User user = User.UserBuilder.anUser()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withPassword(faker.internet().password())
                .build();
        String email = faker.internet().emailAddress();
        User user2 = User.UserBuilder.anUser()
                .withEmail(email)
                .withName(faker.name().fullName())
                .withPassword(faker.internet().password())
                .build();
        userRepository.saveAll(List.of(user, user2));
        String token = jwtService.generateToken(user.getEmail());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        UserDto userDto = new UserDto(null, faker.name().fullName(), email);
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(userDto, headers);
        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "/api/users/current-user", HttpMethod.PUT, httpEntity, UserDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    @Test
    void updateCurrentUser_successTest(){
        User user = User.UserBuilder.anUser()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withPassword(faker.internet().password())
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        UserDto userDto = new UserDto(null, faker.name().fullName(), faker.internet().emailAddress());
        HttpEntity<UserDto> httpEntity = new HttpEntity<>(userDto, headers);
        ResponseEntity<UserDto> response = testRestTemplate.exchange(
                "/api/users/current-user", HttpMethod.PUT, httpEntity, UserDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        UserDto body = response.getBody();
        Assertions.assertThat(body)
                .hasFieldOrPropertyWithValue("email", userDto.email())
                .hasFieldOrPropertyWithValue("name", userDto.name());
    }


    @Test
    void deleteCurrentUser_notAuthenticatedTest(){
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/users/delete-user", HttpMethod.DELETE, httpEntity, Void.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }


    @Test
    void deleteCurrentUser_successTest(){
        User user = User.UserBuilder.anUser()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withPassword(faker.internet().password())
                .build();
        User userResult = userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/users/delete-user", HttpMethod.DELETE, httpEntity, Void.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Optional<User> existingUser = userRepository.findById(userResult.getId());
        Assertions.assertThat(existingUser).isEmpty();
    }

}
