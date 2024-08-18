package dev.mednikov.cashtracker.accounts.controllers;

import dev.mednikov.cashtracker.accounts.dto.PaymentAccountDto;
import dev.mednikov.cashtracker.accounts.models.AccountType;
import dev.mednikov.cashtracker.accounts.models.PaymentAccount;
import dev.mednikov.cashtracker.accounts.repositories.PaymentAccountRepository;
import dev.mednikov.cashtracker.core.BaseIT;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;
import dev.mednikov.cashtracker.users.services.JwtService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.Optional;

class PaymentAccountRestControllerIT extends BaseIT {

    @Autowired private JwtService jwtService;
    @Autowired private UserRepository userRepository;
    @Autowired private PaymentAccountRepository accountRepository;

    @Test
    void createAccountTest(){
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        User userResult = userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        // prepare payload
        PaymentAccountDto payload = new PaymentAccountDto(
                null,
                userResult.getId(),
                faker.lorem().fixedString(15),
                faker.lorem().fixedString(100),
                AccountType.Cash
        );

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<PaymentAccountDto> request = new HttpEntity<>(payload, headers);

        // execute request
        ResponseEntity<PaymentAccountDto> response = testRestTemplate.exchange(
                "/api/accounts/create", HttpMethod.POST, request, PaymentAccountDto.class
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();

        PaymentAccountDto result = response.getBody();
        Assertions.assertThat(result.id()).isNotNull();

    }

    @Test
    void updateAccountTest(){
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        User userResult = userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        PaymentAccount paymentAccount = new PaymentAccount.PaymentAccountBuilder()
                .withOwner(userResult)
                .withAccountType(AccountType.Cash)
                .withName(faker.lorem().fixedString(20))
                .build();
        PaymentAccount result = accountRepository.save(paymentAccount);

        // prepare payload
        PaymentAccountDto payload = new PaymentAccountDto(
                result.getId(),
                userResult.getId(),
                faker.lorem().fixedString(15),
                faker.lorem().fixedString(100),
                AccountType.Cash
        );

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<PaymentAccountDto> request = new HttpEntity<>(payload, headers);

        // execute request
        ResponseEntity<PaymentAccountDto> response = testRestTemplate.exchange(
                "/api/accounts/update", HttpMethod.PUT, request, PaymentAccountDto.class
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

        // assert that entity was updated
        PaymentAccountDto body = response.getBody();
        Assertions
                .assertThat(body)
                .hasFieldOrPropertyWithValue("name", payload.name())
                .hasFieldOrPropertyWithValue("description", payload.description());
    }

    @Test
    void deleteAccountTest(){
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        User userResult = userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        PaymentAccount paymentAccount = new PaymentAccount.PaymentAccountBuilder()
                .withOwner(userResult)
                .withAccountType(AccountType.Cash)
                .withName(faker.lorem().fixedString(20))
                .build();
        PaymentAccount result = accountRepository.save(paymentAccount);
        Long id = result.getId();

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/accounts/delete/{id}", HttpMethod.DELETE, request, Void.class, id
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // verify that entity was removed
        Optional<PaymentAccount> existingAccount = accountRepository.findById(id);
        Assertions.assertThat(existingAccount).isEmpty();

    }

    @Test
    void getAccount_existsTest(){
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        User userResult = userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        PaymentAccount paymentAccount = new PaymentAccount.PaymentAccountBuilder()
                .withOwner(userResult)
                .withAccountType(AccountType.Cash)
                .withName(faker.lorem().fixedString(20))
                .build();
        PaymentAccount result = accountRepository.save(paymentAccount);
        Long id = result.getId();

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<PaymentAccountDto> response = testRestTemplate.exchange(
                "/api/accounts/one/{id}", HttpMethod.GET, request, PaymentAccountDto.class, id
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

    }

    @Test
    void getAccount_doesNotExistTest(){
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        Long id = faker.random().nextLong();

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<PaymentAccountDto> response = testRestTemplate.exchange(
                "/api/accounts/one/{id}", HttpMethod.GET, request, PaymentAccountDto.class, id
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void getAccountsTest(){
        // create user
        User user = new User.UserBuilder()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().fullName())
                .withSuperuser(false)
                .withPassword(faker.internet().password())
                .build();
        userRepository.save(user);

        // create auth token
        String token = jwtService.generateToken(user.getEmail());

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<PaymentAccountDto[]> response = testRestTemplate.exchange(
                "/api/accounts/list", HttpMethod.GET, request, PaymentAccountDto[].class
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
