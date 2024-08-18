package dev.mednikov.cashtracker.accounts.services;

import dev.mednikov.cashtracker.accounts.dto.PaymentAccountDto;
import dev.mednikov.cashtracker.accounts.dto.mappers.PaymentAccountDtoMapper;
import dev.mednikov.cashtracker.accounts.models.PaymentAccount;
import dev.mednikov.cashtracker.accounts.repositories.PaymentAccountRepository;
import dev.mednikov.cashtracker.users.models.User;
import dev.mednikov.cashtracker.users.repositories.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentAccountServiceImpl implements PaymentAccountService {

    private final static PaymentAccountDtoMapper mapper = new PaymentAccountDtoMapper();

    private final UserRepository userRepository;
    private final PaymentAccountRepository paymentAccountRepository;

    public PaymentAccountServiceImpl(
            UserRepository userRepository,
            PaymentAccountRepository paymentAccountRepository)
    {
        this.userRepository = userRepository;
        this.paymentAccountRepository = paymentAccountRepository;
    }

    @Override
    public PaymentAccountDto createAccount(PaymentAccountDto paymentAccountDto) {
        User owner = this.userRepository.getReferenceById(paymentAccountDto.ownerId());
        PaymentAccount account = new PaymentAccount.PaymentAccountBuilder()
                .withOwner(owner)
                .withName(paymentAccountDto.name())
                .withDescription(paymentAccountDto.description())
                .withAccountType(paymentAccountDto.accountType())
                .build();

        PaymentAccount result = this.paymentAccountRepository.save(account);
        return mapper.apply(result);
    }

    @Override
    public PaymentAccountDto updateAccount(PaymentAccountDto paymentAccountDto) {
        PaymentAccount account = this.paymentAccountRepository.findById(paymentAccountDto.id()).orElseThrow();
        account.setName(paymentAccountDto.name());
        account.setDescription(paymentAccountDto.description());
        account.setAccountType(paymentAccountDto.accountType());
        PaymentAccount result = this.paymentAccountRepository.save(account);
        return mapper.apply(result);
    }

    @Override
    public void deleteAccount(Long id) {
        this.paymentAccountRepository.deleteById(id);
    }

    @Override
    public Optional<PaymentAccountDto> getAccount(Long id) {
        return this.paymentAccountRepository.findById(id).map(mapper);
    }

    @Override
    public List<PaymentAccountDto> getAccounts(Long ownerId) {
        return this.paymentAccountRepository.findAllByOwner_Id(ownerId)
                .stream()
                .sorted()
                .map(mapper)
                .toList();
    }
}
