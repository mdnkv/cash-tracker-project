package dev.mednikov.cashtracker.accounts.models;

import dev.mednikov.cashtracker.users.models.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Objects;

@Entity
@Table(name = "paymentaccounts_paymentaccount")
public class PaymentAccount implements Comparable<PaymentAccount> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "account_name", nullable = false)
    private String name;

    @Column(name = "account_description")
    private String description;

    @Override
    public int compareTo(PaymentAccount o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentAccount that)) return false;
        return Objects.equals(owner, that.owner)
                && accountType == that.accountType
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, accountType, name);
    }

    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final class PaymentAccountBuilder {
        private Long id;
        private User owner;
        private AccountType accountType;
        private String name;
        private String description;

        public PaymentAccountBuilder() {
        }

        public PaymentAccountBuilder(PaymentAccount other) {
            this.id = other.id;
            this.owner = other.owner;
            this.accountType = other.accountType;
            this.name = other.name;
            this.description = other.description;
        }

        public static PaymentAccountBuilder aPaymentAccount() {
            return new PaymentAccountBuilder();
        }

        public PaymentAccountBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PaymentAccountBuilder withOwner(User owner) {
            this.owner = owner;
            return this;
        }

        public PaymentAccountBuilder withAccountType(AccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        public PaymentAccountBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public PaymentAccountBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public PaymentAccount build() {
            PaymentAccount paymentAccount = new PaymentAccount();
            paymentAccount.setAccountType(accountType);
            paymentAccount.setName(name);
            paymentAccount.setDescription(description);
            paymentAccount.owner = this.owner;
            paymentAccount.id = this.id;
            return paymentAccount;
        }
    }

}
