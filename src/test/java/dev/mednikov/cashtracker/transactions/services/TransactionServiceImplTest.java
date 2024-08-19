package dev.mednikov.cashtracker.transactions.services;

import com.github.javafaker.Faker;

import dev.mednikov.cashtracker.accounts.repositories.PaymentAccountRepository;
import dev.mednikov.cashtracker.categories.repositories.CategoryRepository;
import dev.mednikov.cashtracker.transactions.dto.TransactionDto;
import dev.mednikov.cashtracker.transactions.models.Transaction;
import dev.mednikov.cashtracker.transactions.models.TransactionType;
import dev.mednikov.cashtracker.transactions.repositories.TransactionRepository;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    private final static Faker faker = new Faker();

    @Mock private UserRepository userRepository;
    @Mock private TransactionRepository transactionRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private PaymentAccountRepository paymentAccountRepository;

    @InjectMocks private TransactionServiceImpl service;

    @Test
    void createTransactionTest(){
        Long ownerId = faker.random().nextLong();
        User owner = User.UserBuilder.anUser().withId(ownerId).build();

        Long transactionId = faker.random().nextLong();
        Transaction transaction = Transaction.TransactionBuilder.aTransaction()
                .withId(transactionId)
                .withTransactionType(TransactionType.Income)
                .withAmount(BigDecimal.valueOf(100.00))
                .withCurrency("EUR")
                .withDescription(faker.lorem().fixedString(100))
                .withTransactionDate(LocalDate.now())
                .withOwner(owner)
                .build();

        TransactionDto transactionDto = TransactionDto.TransactionDtoBuilder.aTransactionDto()
                .withType(TransactionType.Income)
                .withOwnerId(ownerId)
                .withAmount(BigDecimal.valueOf(100.00))
                .withCurrency("EUR")
                .withTransactionDate(LocalDate.now())
                .withDescription(transaction.getDescription())
                .build();

        Mockito.when(userRepository.getReferenceById(ownerId)).thenReturn(owner);
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);

        TransactionDto result = service.createTransaction(transactionDto);
        Assertions.assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", transactionId)
                .hasFieldOrPropertyWithValue("ownerId", ownerId)
                .hasFieldOrPropertyWithValue("type", TransactionType.Income)
                .hasFieldOrPropertyWithValue("amount",transaction.getAmount())
                .hasFieldOrPropertyWithValue("currency", transaction.getCurrency())
                .hasFieldOrPropertyWithValue("description", transaction.getDescription())
                .hasFieldOrPropertyWithValue("transactionDate", transaction.getTransactionDate());

    }

    @Test
    void updateTransactionTest(){
        Long ownerId = faker.random().nextLong();
        User owner = User.UserBuilder.anUser().withId(ownerId).build();

        Long transactionId = faker.random().nextLong();
        Transaction transaction = Transaction.TransactionBuilder.aTransaction()
                .withId(transactionId)
                .withTransactionType(TransactionType.Income)
                .withAmount(BigDecimal.valueOf(100.00))
                .withCurrency("EUR")
                .withDescription(faker.lorem().fixedString(100))
                .withTransactionDate(LocalDate.now())
                .withOwner(owner)
                .build();

        TransactionDto transactionDto = TransactionDto.TransactionDtoBuilder.aTransactionDto()
                .withId(transactionId)
                .withType(TransactionType.Income)
                .withAmount(BigDecimal.valueOf(100.00))
                .withCurrency("EUR")
                .withTransactionDate(LocalDate.now())
                .withDescription(transaction.getDescription())
                .withOwnerId(ownerId)
                .build();

        Mockito.when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        Mockito.when(transactionRepository.save(Mockito.any())).thenReturn(transaction);

        TransactionDto result = service.updateTransaction(transactionDto);
        Assertions.assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", transactionId)
                .hasFieldOrPropertyWithValue("ownerId", ownerId)
                .hasFieldOrPropertyWithValue("type", TransactionType.Income)
                .hasFieldOrPropertyWithValue("amount",transaction.getAmount())
                .hasFieldOrPropertyWithValue("currency", transaction.getCurrency())
                .hasFieldOrPropertyWithValue("description", transaction.getDescription())
                .hasFieldOrPropertyWithValue("transactionDate", transaction.getTransactionDate());
    }

    @Test
    void getTransaction_existsTest(){
        User owner = User.UserBuilder.anUser().withId(faker.random().nextLong()).build();

        Long transactionId = faker.random().nextLong();
        Transaction transaction = Transaction.TransactionBuilder.aTransaction()
                .withId(transactionId)
                .withTransactionType(TransactionType.Income)
                .withAmount(BigDecimal.valueOf(100.00))
                .withCurrency("EUR")
                .withDescription(faker.lorem().fixedString(100))
                .withTransactionDate(LocalDate.now())
                .withOwner(owner)
                .build();

        Mockito.when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        Optional<TransactionDto> result = service.getTransaction(transactionId);
        Assertions.assertThat(result).isPresent();
    }

    @Test
    void getTransaction_doesNotExistTest(){
        Long transactionId = faker.random().nextLong();

        Mockito.when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        Optional<TransactionDto> result = service.getTransaction(transactionId);
        Assertions.assertThat(result).isEmpty();
    }

}
