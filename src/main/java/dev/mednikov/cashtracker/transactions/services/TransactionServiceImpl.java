package dev.mednikov.cashtracker.transactions.services;

import dev.mednikov.cashtracker.accounts.models.PaymentAccount;
import dev.mednikov.cashtracker.accounts.repositories.PaymentAccountRepository;
import dev.mednikov.cashtracker.categories.models.Category;
import dev.mednikov.cashtracker.categories.repositories.CategoryRepository;
import dev.mednikov.cashtracker.transactions.dto.TransactionDto;
import dev.mednikov.cashtracker.transactions.dto.mappers.TransactionDtoMapper;
import dev.mednikov.cashtracker.transactions.models.Transaction;
import dev.mednikov.cashtracker.transactions.repositories.TransactionRepository;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService{

    private final static TransactionDtoMapper mapper = new TransactionDtoMapper();

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentAccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public TransactionServiceImpl(
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            PaymentAccountRepository accountRepository,
            CategoryRepository categoryRepository)
    {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        User owner = this.userRepository.getReferenceById(transactionDto.ownerId());
        Transaction transaction = new Transaction.TransactionBuilder()
                .withOwner(owner)
                .withDescription(transactionDto.description())
                .withAmount(transactionDto.amount())
                .withCurrency(transactionDto.currency())
                .withTransactionDate(transactionDto.transactionDate())
                .withTransactionType(transactionDto.type())
                .build();
        if (transactionDto.categoryId() != null){
            Category category = this.categoryRepository.getReferenceById(transactionDto.categoryId());
            transaction.setCategory(category);
        }
        if (transactionDto.paymentAccountId() != null){
            PaymentAccount payment = this.accountRepository.getReferenceById(transactionDto.paymentAccountId());
            transaction.setPaymentAccount(payment);
        }
        Transaction result = this.transactionRepository.save(transaction);
        return mapper.apply(result);
    }

    @Override
    public TransactionDto updateTransaction(TransactionDto transactionDto) {
        Transaction transaction = this.transactionRepository.findById(transactionDto.id()).orElseThrow();
        transaction.setDescription(transactionDto.description());
        transaction.setAmount(transactionDto.amount());
        transaction.setCurrency(transactionDto.currency());
        transaction.setTransactionDate(transactionDto.transactionDate());
        transaction.setTransactionType(transactionDto.type());


        if (transactionDto.paymentAccountId() != null){
            PaymentAccount payment = this.accountRepository.getReferenceById(transactionDto.paymentAccountId());
            transaction.setPaymentAccount(payment);
        }

        if (transactionDto.categoryId() != null){
            Category category = this.categoryRepository.getReferenceById(transactionDto.categoryId());
            transaction.setCategory(category);
        }

        Transaction result = this.transactionRepository.save(transaction);
        return mapper.apply(result);
    }

    @Override
    public void deleteTransaction(Long id) {
        this.transactionRepository.deleteById(id);
    }

    @Override
    public Optional<TransactionDto> getTransaction(Long id) {
        return this.transactionRepository.findById(id).map(mapper);
    }

    @Override
    public List<TransactionDto> getTransactions(Long ownerId) {
        return this.transactionRepository.findAllForOwner(ownerId)
                .stream()
                .sorted(Comparator.reverseOrder())
                .map(mapper)
                .toList();
    }
}
