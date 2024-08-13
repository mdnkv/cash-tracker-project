package dev.mednikov.cashtracker.users.services;

import dev.mednikov.cashtracker.users.dto.CreateUserDto;
import dev.mednikov.cashtracker.users.dto.LoginRequestDto;
import dev.mednikov.cashtracker.users.dto.LoginResponseDto;
import dev.mednikov.cashtracker.users.dto.UserDto;
import dev.mednikov.cashtracker.users.dto.mappers.UserDtoMapper;
import dev.mednikov.cashtracker.users.exceptions.LoginException;
import dev.mednikov.cashtracker.users.exceptions.UserAlreadyExistsException;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final static UserDtoMapper userDtoMapper = new UserDtoMapper();

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserServiceImpl(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            JwtService jwtService)
    {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public UserDto createUser(CreateUserDto requestDto) {
        // check that user does not exist yet
        Optional<User> existingUser = this.userRepository.findByEmail(requestDto.email());
        if (existingUser.isPresent()) {
            // throw an exception
            throw new UserAlreadyExistsException(requestDto.email());
        }

        // 1st user is a superuser
        boolean superuser = this.userRepository.count() == 0;

        // hash password
        String hash = this.passwordEncoder.encode(requestDto.password());

        // create user
        User user = User.UserBuilder.anUser()
                .withEmail(requestDto.email())
                .withPassword(hash)
                .withName(requestDto.name())
                .withSuperuser(superuser)
                .build();

        // save user
        User result = this.userRepository.save(user);

        // return result
        return userDtoMapper.apply(result);
    }

    @Override
    public LoginResponseDto loginWithEmailAndPassword(LoginRequestDto requestDto) {
        // check that user exists
        User user = this.userRepository.findByEmail(requestDto.email()).orElseThrow(LoginException::new);

        // check password
        if (!this.passwordEncoder.matches(requestDto.password(), user.getPassword())){
            throw new LoginException();
        }

        // issue token
        String token = this.jwtService.generateToken(user.getEmail());

        // return result
        return new LoginResponseDto(token, user.getId());
    }

    @Override
    public UserDto getCurrentUser(Long userId) {
        User user = this.userRepository.findById(userId).orElseThrow();
        return userDtoMapper.apply(user);
    }

    @Override
    public UserDto updateCurrentUser(UserDto userDto) {
        User user = this.userRepository.findById(userDto.id()).orElseThrow();
        if (!user.getEmail().equals(userDto.email())) {
            // check email does not belong to another user
            Optional<User> existingUser = this.userRepository.findByEmail(userDto.email());
            if (existingUser.isPresent()) {
                throw new UserAlreadyExistsException(userDto.email());
            }
            user.setEmail(userDto.email());
        }
        // do update
        user.setName(userDto.name());

        User result = this.userRepository.save(user);
        return userDtoMapper.apply(result);
    }

    @Override
    public void deleteCurrentUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
