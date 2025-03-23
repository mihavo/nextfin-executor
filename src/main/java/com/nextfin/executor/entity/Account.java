package com.nextfin.executor.entity;

import com.nextfin.executor.enums.AccountStatus;
import com.nextfin.executor.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Represents the Account entity that stores the user's bank accounts
 */
@Entity
@DynamicUpdate
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Account {
    @Id
    @SequenceGenerator(name = "accounts_gen", sequenceName = "accounts_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_gen")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "balance", nullable = false)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Enumerated
    private AccountStatus status;

    @Enumerated
    @Builder.Default
    private AccountType accountType = AccountType.SAVINGS;


    @Column(name = "transaction_limit")
    @Builder.Default
    Long transactionLimit = 1000L;


}
