package dev.mednikov.cashtracker.transactions.dto;

import dev.mednikov.cashtracker.transactions.models.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDto(
        Long id,
        @NotNull Long ownerId,
        @NotNull @NotBlank @Size(max=250) String description,
        @NotNull @NotBlank @Size(max = 3) String currency,
        @NotNull @Min(0) BigDecimal amount,
        @NotNull LocalDate transactionDate,
        @NotNull TransactionType type,
        Long paymentAccountId,
        Long categoryId
) {
    public static final class TransactionDtoBuilder {
        private Long id;
        private Long ownerId;
        private String description;
        private String currency;
        private BigDecimal amount;
        private LocalDate transactionDate;
        private TransactionType type;
        private Long paymentAccountId;
        private Long categoryId;

        public TransactionDtoBuilder() {
        }

        public TransactionDtoBuilder(TransactionDto other) {
            this.id = other.id();
            this.ownerId = other.ownerId();
            this.description = other.description();
            this.currency = other.currency();
            this.amount = other.amount();
            this.transactionDate = other.transactionDate();
            this.type = other.type();
            this.paymentAccountId = other.paymentAccountId();
            this.categoryId = other.categoryId();
        }

        public static TransactionDtoBuilder aTransactionDto() {
            return new TransactionDtoBuilder();
        }

        public TransactionDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public TransactionDtoBuilder withOwnerId(Long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public TransactionDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public TransactionDtoBuilder withCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public TransactionDtoBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransactionDtoBuilder withTransactionDate(LocalDate transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public TransactionDtoBuilder withType(TransactionType type) {
            this.type = type;
            return this;
        }

        public TransactionDtoBuilder withPaymentAccountId(Long paymentAccountId) {
            this.paymentAccountId = paymentAccountId;
            return this;
        }

        public TransactionDtoBuilder withCategoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public TransactionDto build() {
            return new TransactionDto(id, ownerId, description, currency, amount, transactionDate, type, paymentAccountId, categoryId);
        }
    }
}
