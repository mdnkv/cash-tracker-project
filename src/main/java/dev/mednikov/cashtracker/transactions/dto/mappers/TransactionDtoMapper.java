package dev.mednikov.cashtracker.transactions.dto.mappers;

import dev.mednikov.cashtracker.accounts.dto.mappers.PaymentAccountDtoMapper;
import dev.mednikov.cashtracker.categories.dto.mappers.CategoryDtoMapper;
import dev.mednikov.cashtracker.transactions.dto.TransactionDto;
import dev.mednikov.cashtracker.transactions.models.Transaction;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.function.Function;

public final class TransactionDtoMapper implements Function<Transaction, TransactionDto> {

    private final static CategoryDtoMapper categoryDtoMapper = new CategoryDtoMapper();
    private final static PaymentAccountDtoMapper paymentAccountDtoMapper = new PaymentAccountDtoMapper();

    @Override
    public TransactionDto apply(Transaction transaction) {
        CurrencyUnit currencyUnit = CurrencyUnit.of(transaction.getCurrency());
        Money money = Money.of(currencyUnit, transaction.getAmount());
        String displayedAmount = money.toString();
        return new TransactionDto.TransactionDtoBuilder()
                .withId(transaction.getId())
                .withOwnerId(transaction.getOwner().getId())
                .withAmount(transaction.getAmount())
                .withDescription(transaction.getDescription())
                .withCurrency(transaction.getCurrency())
                .withDescription(transaction.getDescription())
                .withTransactionDate(transaction.getTransactionDate())
                .withType(transaction.getTransactionType())
                .withDisplayedAmount(displayedAmount)
                .withCategory(
                        transaction.getCategory() != null
                                ? categoryDtoMapper.apply(transaction.getCategory())
                                : null
                )
                .withAccount(
                        transaction.getPaymentAccount() != null
                                ? paymentAccountDtoMapper.apply(transaction.getPaymentAccount())
                                : null
                )
                .withCategoryId(
                        transaction.getCategory() != null
                        ? transaction.getCategory().getId()
                        : null
                )
                .withPaymentAccountId(
                        transaction.getPaymentAccount() != null
                                ? transaction.getPaymentAccount().getId()
                                : null
                )
                .build();
    }
}
