package dev.mednikov.cashtracker.transactions.dto.mappers;

import dev.mednikov.cashtracker.transactions.dto.TransactionDto;
import dev.mednikov.cashtracker.transactions.models.Transaction;

import java.util.function.Function;

public final class TransactionDtoMapper implements Function<Transaction, TransactionDto> {

    @Override
    public TransactionDto apply(Transaction transaction) {
        return new TransactionDto.TransactionDtoBuilder()
                .withId(transaction.getId())
                .withOwnerId(transaction.getOwner().getId())
                .withAmount(transaction.getAmount())
                .withDescription(transaction.getDescription())
                .withCurrency(transaction.getCurrency())
                .withDescription(transaction.getDescription())
                .withTransactionDate(transaction.getTransactionDate())
                .withType(transaction.getTransactionType())
                .build();
    }
}
