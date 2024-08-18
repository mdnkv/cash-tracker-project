package dev.mednikov.cashtracker.accounts.controllers;

import dev.mednikov.cashtracker.accounts.dto.PaymentAccountDto;
import dev.mednikov.cashtracker.accounts.services.PaymentAccountService;
import dev.mednikov.cashtracker.users.models.User;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class PaymentAccountRestController {

    private final PaymentAccountService paymentAccountService;

    public PaymentAccountRestController(PaymentAccountService paymentAccountService) {
        this.paymentAccountService = paymentAccountService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody PaymentAccountDto createAccount(@RequestBody @Valid PaymentAccountDto paymentAccountDto) {
        return paymentAccountService.createAccount(paymentAccountDto);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody PaymentAccountDto updateAccount(@RequestBody @Valid PaymentAccountDto paymentAccountDto) {
        return paymentAccountService.updateAccount(paymentAccountDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount (@PathVariable Long id) {
        this.paymentAccountService.deleteAccount(id);
    }

    @GetMapping("/one/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PaymentAccountDto> getAccount(@PathVariable Long id) {
        Optional<PaymentAccountDto> result = this.paymentAccountService.getAccount(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<PaymentAccountDto> getAllAccounts(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return this.paymentAccountService.getAccounts(user.getId());
    }

}
