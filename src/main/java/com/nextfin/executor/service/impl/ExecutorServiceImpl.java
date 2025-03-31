package com.nextfin.executor.service.impl;

import com.nextfin.account.dto.DepositDto;
import com.nextfin.account.dto.WithdrawDto;
import com.nextfin.account.entity.Account;
import com.nextfin.account.service.AccountService;
import com.nextfin.executor.currency.CurrencyConverterService;
import com.nextfin.executor.service.ExecutorService;
import com.nextfin.transaction.entity.Transaction;
import com.nextfin.transaction.enums.TransactionStatus;
import com.nextfin.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Currency;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExecutorServiceImpl implements ExecutorService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final CurrencyConverterService currencyConverter;

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
        Currency requestCurrency = transaction.getCurrency();
        BigDecimal amount = transaction.getAmount();
        //Convert amount to target & source account's default currency
        Pair<BigDecimal, BigDecimal> targetAmounts = currencyConverter.convert(amount, requestCurrency,
                                                                               sourceAccount.getCurrency(),
                                                                               targetAccount.getCurrency());

        accountService.withdrawAmount(sourceAccount.getId(), new WithdrawDto(targetAmounts.a));
        accountService.depositAmount(targetAccount.getId(), new DepositDto(targetAmounts.b));
    }
}
