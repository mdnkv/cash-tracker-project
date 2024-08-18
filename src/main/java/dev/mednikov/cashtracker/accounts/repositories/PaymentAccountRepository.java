package dev.mednikov.cashtracker.accounts.repositories;

import dev.mednikov.cashtracker.accounts.models.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Long> {

    List<PaymentAccount> findAllByOwner_Id (Long id);

}
