package dev.mednikov.cashtracker.accounts.services;

import dev.mednikov.cashtracker.accounts.dto.PaymentAccountDto;

import java.util.List;
import java.util.Optional;

public interface PaymentAccountService {

    PaymentAccountDto createAccount(PaymentAccountDto paymentAccountDto);

    PaymentAccountDto updateAccount(PaymentAccountDto paymentAccountDto);

    void deleteAccount (Long id);

    Optional<PaymentAccountDto> getAccount(Long id);

    List<PaymentAccountDto> getAccounts(Long ownerId);

}
