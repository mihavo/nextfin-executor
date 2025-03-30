package com.nextfin.executor.service.impl;

import com.nextfin.account.dto.DepositDto;
import com.nextfin.account.dto.WithdrawDto;
import com.nextfin.account.entity.Account;
import com.nextfin.account.service.AccountService;
import com.nextfin.executor.service.ExecutorService;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.enums.TransactionStatus;
import com.nextfin.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;
import java.util.Currency;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExecutorServiceImpl implements ExecutorService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Override
    public Transaction process(Transaction transaction) {
        transaction.setTransactionStatus(TransactionStatus.PROCESSING);
        transactionRepository.save(transaction);
        log.debug("Transaction with id {} submitted for processing", transaction.getId());
        execute(transaction);
        log.debug("Transaction with id {} completed", transaction.getId());
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
        return transactionRepository.save(transaction);
    }

    private void execute(Transaction transaction) {
        Account sourceAccount = transaction.getSourceAccount();
        Account targetAccount = transaction.getTargetAccount();
        Currency currency = transaction.getCurrency();
        BigDecimal amount = transaction.getAmount();
        //Convert amount to target & source account's default currency
        CurrencyConversion requestToTargetConversion = MonetaryConversions.getConversion(
                targetAccount.getCurrency().getCurrencyCode());
        CurrencyConversion requestToSourceConversion = MonetaryConversions.getConversion(
                sourceAccount.getCurrency().getCurrencyCode());
        Money requestedCurrencyAmount = Money.of(amount, currency.getCurrencyCode());
        Money targetCurrencyAmount = requestedCurrencyAmount.with(
                requestToTargetConversion); //amount in source account's currency
        Money sourceCurrencyAmount = requestedCurrencyAmount.with(
                requestToSourceConversion); //amount in target account's currency


        accountService.withdrawAmount(sourceAccount.getId(),
                                      new WithdrawDto(sourceCurrencyAmount.getNumber().numberValue(BigDecimal.class)));
        accountService.depositAmount(targetAccount.getId(),
                                     new DepositDto(targetCurrencyAmount.getNumber().numberValue(BigDecimal.class)));
    }
}
