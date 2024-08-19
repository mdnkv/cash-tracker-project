package dev.mednikov.cashtracker.transactions.services;

import dev.mednikov.cashtracker.transactions.dto.TransactionDto;

import java.util.List;
import java.util.Optional;

public interface TransactionService {

    TransactionDto createTransaction(TransactionDto transactionDto);

    TransactionDto updateTransaction(TransactionDto transactionDto);

    void deleteTransaction (Long id);

    Optional<TransactionDto> getTransaction(Long id);

    List<TransactionDto> getTransactions(Long ownerId);

}
