package dev.mednikov.cashtracker.transactions.controllers;

import dev.mednikov.cashtracker.core.BaseIT;
import dev.mednikov.cashtracker.transactions.dto.TransactionDto;
import dev.mednikov.cashtracker.transactions.models.Transaction;
import dev.mednikov.cashtracker.transactions.models.TransactionType;
import dev.mednikov.cashtracker.transactions.repositories.TransactionRepository;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;
import dev.mednikov.cashtracker.users.services.JwtService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

class TransactionRestControllerTest extends BaseIT {

    @Autowired private JwtService jwtService;
    @Autowired private UserRepository userRepository;
    @Autowired private TransactionRepository transactionRepository;

    @Test
    void createTransactionTest() {
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
        TransactionDto payload = new TransactionDto.TransactionDtoBuilder()
                .withOwnerId(userResult.getId())
                .withDescription(faker.lorem().fixedString(100))
                .withType(TransactionType.Expense)
                .withCurrency("EUR")
                .withAmount(BigDecimal.valueOf(1000))
                .withTransactionDate(LocalDate.now())
                .build();

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<TransactionDto> request = new HttpEntity<>(payload, headers);

        // execute request
        ResponseEntity<TransactionDto> response = testRestTemplate.exchange(
                "/api/transactions/create", HttpMethod.POST, request, TransactionDto.class
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();

        TransactionDto body = response.getBody();
        Assertions.assertThat(body.id()).isNotNull();

    }

    @Test
    void updateTransactionTest() {
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

        // create existing transaction
        Transaction transaction = new Transaction.TransactionBuilder()
                .withOwner(userResult)
                .withDescription(faker.lorem().fixedString(150))
                .withTransactionType(TransactionType.Expense)
                .withCurrency("EUR")
                .withAmount(BigDecimal.valueOf(900))
                .withTransactionDate(LocalDate.now())
                .build();
        Transaction transactionResult = transactionRepository.save(transaction);

        // prepare payload
        TransactionDto payload = new TransactionDto.TransactionDtoBuilder()
                .withId(transactionResult.getId())
                .withOwnerId(userResult.getId())
                .withDescription(faker.lorem().fixedString(100))
                .withType(TransactionType.Income)
                .withCurrency("EUR")
                .withAmount(BigDecimal.valueOf(1000))
                .withTransactionDate(LocalDate.now())
                .build();

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<TransactionDto> request = new HttpEntity<>(payload, headers);

        // execute request
        ResponseEntity<TransactionDto> response = testRestTemplate.exchange(
                "/api/transactions/update", HttpMethod.PUT, request, TransactionDto.class
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

        TransactionDto body = response.getBody();
        Assertions.assertThat(body)
                .hasFieldOrPropertyWithValue("amount", payload.amount())
                .hasFieldOrPropertyWithValue("description", payload.description())
                .hasFieldOrPropertyWithValue("type", payload.type());

    }

    @Test
    void deleteTransactionTest() {
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

        // create existing transaction
        Transaction transaction = new Transaction.TransactionBuilder()
                .withOwner(userResult)
                .withDescription(faker.lorem().fixedString(150))
                .withTransactionType(TransactionType.Expense)
                .withCurrency("EUR")
                .withAmount(BigDecimal.valueOf(900))
                .withTransactionDate(LocalDate.now())
                .build();
        Transaction transactionResult = transactionRepository.save(transaction);
        Long transactionId = transactionResult.getId();

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/api/transactions/delete/{id}", HttpMethod.DELETE, request, Void.class, transactionId
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // assert that transaction was deleted
        Optional<Transaction> result = transactionRepository.findById(transactionId);
        Assertions.assertThat(result).isEmpty();

    }

    @Test
    void getTransactionById_existsTest() {
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

        // create existing transaction
        Transaction transaction = new Transaction.TransactionBuilder()
                .withOwner(userResult)
                .withDescription(faker.lorem().fixedString(150))
                .withTransactionType(TransactionType.Expense)
                .withCurrency("EUR")
                .withAmount(BigDecimal.valueOf(900))
                .withTransactionDate(LocalDate.now())
                .build();
        Transaction transactionResult = transactionRepository.save(transaction);
        Long transactionId = transactionResult.getId();

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<TransactionDto> response = testRestTemplate.exchange(
                "/api/transactions/one/{id}", HttpMethod.GET, request, TransactionDto.class, transactionId
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

    }

    @Test
    void getTransactionById_notExistsTest() {
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

        // generate non existing transaction id
        Long transactionId = faker.random().nextLong();

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<TransactionDto> response = testRestTemplate.exchange(
                "/api/transactions/one/{id}", HttpMethod.GET, request, TransactionDto.class, transactionId
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void getTransactionsTest() {
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

        // create existing transaction
        Transaction transaction = new Transaction.TransactionBuilder()
                .withOwner(userResult)
                .withDescription(faker.lorem().fixedString(150))
                .withTransactionType(TransactionType.Expense)
                .withCurrency("EUR")
                .withAmount(BigDecimal.valueOf(900))
                .withTransactionDate(LocalDate.now())
                .build();
        transactionRepository.save(transaction);

        // prepare request
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        // execute request
        ResponseEntity<TransactionDto[]> response = testRestTemplate.exchange(
                "/api/transactions/list", HttpMethod.GET, request, TransactionDto[].class
        );

        // assert results
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();

        TransactionDto[] body = response.getBody();
        Assertions.assertThat(body.length).isEqualTo(1);

    }

}
