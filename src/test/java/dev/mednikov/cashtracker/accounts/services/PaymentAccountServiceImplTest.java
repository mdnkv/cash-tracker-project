package dev.mednikov.cashtracker.accounts.services;

import com.github.javafaker.Faker;

import dev.mednikov.cashtracker.accounts.dto.PaymentAccountDto;
import dev.mednikov.cashtracker.accounts.models.AccountType;
import dev.mednikov.cashtracker.accounts.models.PaymentAccount;
import dev.mednikov.cashtracker.accounts.repositories.PaymentAccountRepository;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PaymentAccountServiceImplTest {

    private final static Faker faker = new Faker();

    @Mock private UserRepository userRepository;
    @Mock private PaymentAccountRepository paymentAccountRepository;
    @InjectMocks private PaymentAccountServiceImpl paymentAccountService;

    @Test
    void createAccountTest(){
        Long userId = faker.random().nextLong();
        User user = new User.UserBuilder()
                .withId(userId)
                .withEmail(faker.internet().emailAddress())
                .build();

        PaymentAccount account = new PaymentAccount.PaymentAccountBuilder()
                .withId(faker.random().nextLong())
                .withName(faker.lorem().fixedString(25))
                .withDescription(faker.lorem().fixedString(100))
                .withAccountType(AccountType.Bank)
                .withOwner(user)
                .build();
        PaymentAccountDto requestDto = new PaymentAccountDto(
                null,
                userId,
                account.getName(),
                account.getDescription(),
                account.getAccountType()
        );

        Mockito.when(userRepository.getReferenceById(userId)).thenReturn(user);
        Mockito.when(paymentAccountRepository.save(Mockito.any())).thenReturn(account);

        PaymentAccountDto result = paymentAccountService.createAccount(requestDto);
        Assertions.assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("id", account.getId())
                .hasFieldOrPropertyWithValue("ownerId", userId)
                .hasFieldOrPropertyWithValue("name", account.getName())
                .hasFieldOrPropertyWithValue("description", account.getDescription())
                .hasFieldOrPropertyWithValue("accountType", account.getAccountType());
    }

    @Test
    void updateAccountTest(){
        Long userId = faker.random().nextLong();
        User user = new User.UserBuilder()
                .withId(userId)
                .withEmail(faker.internet().emailAddress())
                .build();

        Long accountId = faker.random().nextLong();
        PaymentAccount account = new PaymentAccount.PaymentAccountBuilder()
                .withId(accountId)
                .withName(faker.lorem().fixedString(25))
                .withDescription(faker.lorem().fixedString(100))
                .withAccountType(AccountType.Bank)
                .withOwner(user)
                .build();
        PaymentAccountDto requestDto = new PaymentAccountDto(
                accountId,
                userId,
                account.getName(),
                account.getDescription(),
                account.getAccountType()
        );
        Mockito.when(paymentAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        Mockito.when(paymentAccountRepository.save(Mockito.any())).thenReturn(account);

        PaymentAccountDto result = paymentAccountService.updateAccount(requestDto);
        Assertions.assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("id", accountId)
                .hasFieldOrPropertyWithValue("ownerId", userId)
                .hasFieldOrPropertyWithValue("name", account.getName())
                .hasFieldOrPropertyWithValue("description", account.getDescription())
                .hasFieldOrPropertyWithValue("accountType", account.getAccountType());

    }

    @Test
    void getAccount_existsTest(){
        Long userId = faker.random().nextLong();
        User user = new User.UserBuilder()
                .withId(userId)
                .withEmail(faker.internet().emailAddress())
                .build();

        Long accountId = faker.random().nextLong();
        PaymentAccount account = new PaymentAccount.PaymentAccountBuilder()
                .withId(accountId)
                .withName(faker.lorem().fixedString(25))
                .withDescription(faker.lorem().fixedString(100))
                .withAccountType(AccountType.Bank)
                .withOwner(user)
                .build();

        Mockito.when(paymentAccountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Optional<PaymentAccountDto> result  = paymentAccountService.getAccount(accountId);
        Assertions.assertThat(result).isPresent();

    }

    @Test
    void getAccount_doesNotExistTest(){
        Long accountId = faker.random().nextLong();

        Mockito.when(paymentAccountRepository.findById(accountId)).thenReturn(Optional.empty());

        Optional<PaymentAccountDto> result  = paymentAccountService.getAccount(accountId);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getAccountsTest(){
        Long userId = faker.random().nextLong();
        User user = new User.UserBuilder()
                .withId(userId)
                .withEmail(faker.internet().emailAddress())
                .build();

        List<PaymentAccount> accounts = List.of(
                new PaymentAccount.PaymentAccountBuilder()
                        .withId(faker.random().nextLong())
                        .withName(faker.lorem().fixedString(25))
                        .withDescription(faker.lorem().fixedString(100))
                        .withAccountType(AccountType.Bank)
                        .withOwner(user)
                        .build(),
                new PaymentAccount.PaymentAccountBuilder()
                        .withId(faker.random().nextLong())
                        .withName(faker.lorem().fixedString(25))
                        .withDescription(faker.lorem().fixedString(100))
                        .withAccountType(AccountType.Bank)
                        .withOwner(user)
                        .build()
        );

        Mockito.when(paymentAccountRepository.findAllByOwner_Id(userId)).thenReturn(accounts);

        List<PaymentAccountDto> result  = paymentAccountService.getAccounts(userId);
        Assertions.assertThat(result).isNotNull().hasSize(2);

    }

}
