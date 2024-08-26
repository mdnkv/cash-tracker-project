package dev.mednikov.cashtracker.transactions.models;

import dev.mednikov.cashtracker.accounts.models.PaymentAccount;
import dev.mednikov.cashtracker.categories.models.Category;
import dev.mednikov.cashtracker.users.models.User;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "transactions_transaction")
public class Transaction implements Comparable<Transaction> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "payment_account_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private PaymentAccount paymentAccount;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Category category;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "transaction_description")
    private String description;

    @Column(name = "transaction_currency", nullable = false)
    private String currency;

    @Column(name = "transaction_amount", nullable = false)
    @ColumnDefault("0.0")
    private BigDecimal amount;

    @Override
    public int compareTo(Transaction o) {
        return this.transactionDate.compareTo(o.transactionDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(owner, that.owner)
                && transactionType == that.transactionType
                && Objects.equals(transactionDate, that.transactionDate)
                && Objects.equals(currency, that.currency)
                && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, transactionType, transactionDate, currency, amount);
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public PaymentAccount getPaymentAccount() {
        return paymentAccount;
    }

    public Category getCategory() {
        return category;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setPaymentAccount(PaymentAccount paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public static final class TransactionBuilder {
        private Long id;
        private User owner;
        private PaymentAccount paymentAccount;
        private Category category;
        private TransactionType transactionType;
        private LocalDate transactionDate;
        private String description;
        private String currency;
        private BigDecimal amount;

        public TransactionBuilder() {
        }

        public TransactionBuilder(Transaction other) {
            this.id = other.id;
            this.owner = other.owner;
            this.paymentAccount = other.paymentAccount;
            this.category = other.category;
            this.transactionType = other.transactionType;
            this.transactionDate = other.transactionDate;
            this.description = other.description;
            this.currency = other.currency;
            this.amount = other.amount;
        }

        public static TransactionBuilder aTransaction() {
            return new TransactionBuilder();
        }

        public TransactionBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public TransactionBuilder withOwner(User owner) {
            this.owner = owner;
            return this;
        }

        public TransactionBuilder withPaymentAccount(PaymentAccount paymentAccount) {
            this.paymentAccount = paymentAccount;
            return this;
        }

        public TransactionBuilder withCategory(Category category) {
            this.category = category;
            return this;
        }

        public TransactionBuilder withTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public TransactionBuilder withTransactionDate(LocalDate transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public TransactionBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public TransactionBuilder withCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public TransactionBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Transaction build() {
            Transaction transaction = new Transaction();
            transaction.setPaymentAccount(paymentAccount);
            transaction.setCategory(category);
            transaction.setTransactionType(transactionType);
            transaction.setTransactionDate(transactionDate);
            transaction.setDescription(description);
            transaction.setCurrency(currency);
            transaction.setAmount(amount);
            transaction.id = this.id;
            transaction.owner = this.owner;
            return transaction;
        }
    }

}
