package dev.mednikov.cashtracker.transactions.controllers;

import dev.mednikov.cashtracker.transactions.dto.TransactionDto;
import dev.mednikov.cashtracker.transactions.services.TransactionService;
import dev.mednikov.cashtracker.users.models.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {

    private final TransactionService transactionService;

    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody TransactionDto createTransaction(@RequestBody @Valid TransactionDto transactionDto) {
        return this.transactionService.createTransaction(transactionDto);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody TransactionDto updateTransaction(@RequestBody @Valid TransactionDto transactionDto) {
        return this.transactionService.updateTransaction(transactionDto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction (@PathVariable Long id){
        this.transactionService.deleteTransaction(id);
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id){
        Optional<TransactionDto> result = this.transactionService.getTransaction(id);
        return ResponseEntity.of(result);
    }

    @GetMapping("/list")
    public List<TransactionDto> getAllTransactions(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return this.transactionService.getTransactions(user.getId());
    }

}
