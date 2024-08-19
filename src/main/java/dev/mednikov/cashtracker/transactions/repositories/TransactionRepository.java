package dev.mednikov.cashtracker.transactions.repositories;

import dev.mednikov.cashtracker.transactions.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.owner.id = :ownerId order by t.transactionDate desc")
    List<Transaction> findAllForOwner (Long ownerId);

}
