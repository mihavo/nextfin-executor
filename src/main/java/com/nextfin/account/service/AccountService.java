package com.nextfin.account.service;

import com.nextfin.account.dto.DepositDto;
import com.nextfin.account.dto.WithdrawDto;
import com.nextfin.account.entity.Account;
import com.nextfin.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final MessageSource messageSource;


    public BigDecimal depositAmount(Long accountId, DepositDto dto) {
        Account account = getAccount(accountId);
        account.setBalance(account.getBalance().add(dto.amount()));
        accountRepository.save(account);
        log.debug("Account {} deposited with amount {}", account, dto.amount());
        return account.getBalance();
    }

    public BigDecimal withdrawAmount(Long accountId, WithdrawDto dto) {
        Account account = getAccount(accountId);
        BigDecimal balance = account.getBalance();
        BigDecimal withdrawAmount = dto.amount();
        if (balance.compareTo(withdrawAmount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                              messageSource.getMessage("account.withdraw.insufficient", null,
                                                                       LocaleContextHolder.getLocale()));
        }
        account.setBalance(balance.subtract(withdrawAmount));
        accountRepository.save(account);
        log.debug("Account {} withdrawn with amount {}", account, withdrawAmount);
        return account.getBalance();
    }

    public Account getAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                  messageSource.getMessage("account.notfound", new Long[]{accountId},
                                                                           LocaleContextHolder.getLocale())));
        log.debug("Account fetched: {}", account);
        return account;
    }
}
