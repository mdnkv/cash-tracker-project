package dev.mednikov.cashtracker.accounts.dto.mappers;

import dev.mednikov.cashtracker.accounts.dto.PaymentAccountDto;
import dev.mednikov.cashtracker.accounts.models.PaymentAccount;

import java.util.function.Function;

public final class PaymentAccountDtoMapper
        implements Function<PaymentAccount, PaymentAccountDto> {


    @Override
    public PaymentAccountDto apply(PaymentAccount paymentAccount) {
        return new PaymentAccountDto(
                paymentAccount.getId(),
                paymentAccount.getOwner().getId(),
                paymentAccount.getName(),
                paymentAccount.getDescription(),
                paymentAccount.getAccountType()
        );
    }
}
