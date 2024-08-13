package dev.mednikov.cashtracker.users.services;

import com.github.javafaker.Faker;
import dev.mednikov.cashtracker.users.dto.CreateUserDto;
import dev.mednikov.cashtracker.users.dto.LoginRequestDto;
import dev.mednikov.cashtracker.users.dto.LoginResponseDto;
import dev.mednikov.cashtracker.users.dto.UserDto;
import dev.mednikov.cashtracker.users.exceptions.LoginException;
import dev.mednikov.cashtracker.users.exceptions.UserAlreadyExistsException;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final static Faker faker = new Faker();

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;

    @InjectMocks private UserServiceImpl userService;

    @Test
    void createUser_alreadyExistsTest(){
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();

        CreateUserDto requestDto = new CreateUserDto(email, password, name);
        User user = User.UserBuilder.anUser().withEmail(email).build();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Assertions.assertThatThrownBy(() -> userService.createUser(requestDto)).isInstanceOf(UserAlreadyExistsException.class);

    }

    @Test
    void createUser_successTest(){
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().fullName();
        CreateUserDto requestDto = new CreateUserDto(email, password, name);

        Long userId = faker.random().nextLong();

        User user = User.UserBuilder.anUser()
                .withId(userId)
                .withPassword(password)
                .withName(name)
                .withEmail(email).build();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(password);

        UserDto result = userService.createUser(requestDto);
        Assertions.assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("id", userId)
                .hasFieldOrPropertyWithValue("email", email)
                .hasFieldOrPropertyWithValue("name", name);

    }

    @Test
    void login_userDoesNotExistTest(){
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        LoginRequestDto requestDto = new LoginRequestDto(email, password);

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.loginWithEmailAndPassword(requestDto)).isInstanceOf(LoginException.class);

    }

    @Test
    void login_passwordDoesNotMatchTest(){

        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String userPassword = faker.internet().password();
        LoginRequestDto requestDto = new LoginRequestDto(email, password);
        Long userId = faker.random().nextLong();
        User user = User.UserBuilder.anUser()
                .withId(userId)
                .withPassword(userPassword)
                .withEmail(email).build();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(password, userPassword)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> userService.loginWithEmailAndPassword(requestDto)).isInstanceOf(LoginException.class);

    }

    @Test
    void login_successTest(){
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String userPassword = faker.internet().password();
        String token = "some-valid-token";
        LoginRequestDto requestDto = new LoginRequestDto(email, password);
        Long userId = faker.random().nextLong();
        User user = User.UserBuilder.anUser()
                .withId(userId)
                .withPassword(userPassword)
                .withEmail(email).build();

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(password, userPassword)).thenReturn(true);
        Mockito.when(jwtService.generateToken(email)).thenReturn(token);

        LoginResponseDto result = userService.loginWithEmailAndPassword(requestDto);
        Assertions.assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("token", token)
                .hasFieldOrPropertyWithValue("id", userId);

    }

    @Test
    void updateCurrentUser_emailAlreadyExistsTest(){
        String email = faker.internet().emailAddress();
        String newEmail = faker.internet().emailAddress();
        String name = faker.name().fullName();
        Long userId = faker.random().nextLong();
        UserDto userDto = new UserDto(userId, name, newEmail);
        User user = User.UserBuilder.anUser().withEmail(email).build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail(newEmail)).thenReturn(Optional.of(new User()));

        Assertions.assertThatThrownBy(() -> userService.updateCurrentUser(userDto)).isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void updateCurrentUser_successTest(){
        String email = faker.internet().emailAddress();
        Long userId = faker.random().nextLong();
        String name = faker.name().fullName();
        UserDto userDto = new UserDto(userId, name, email);
        User user = User.UserBuilder.anUser()
                .withEmail(email)
                .withId(userId)
                .withName(name).build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        final UserDto result = userService.updateCurrentUser(userDto);

        Assertions.assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("email", email)
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("id", userId);

    }

    @Test
    void getCurrentUserTest(){
        String email = faker.internet().emailAddress();
        String name = faker.name().fullName();
        Long userId = faker.random().nextLong();
        final User user = User.UserBuilder.anUser()
                .withEmail(email)
                .withId(userId)
                .withName(name).build();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        final UserDto result = userService.getCurrentUser(userId);

        Assertions.assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("email", email)
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("id", userId);

    }

}
