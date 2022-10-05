package com.ippon.exercise.bankappevent.domain.service;

import com.ippon.exercise.bankappevent.domain.exception.AccountError;
import com.ippon.exercise.bankappevent.domain.exception.AccountNotFoundException;
import com.ippon.exercise.bankappevent.domain.model.Account;
import com.ippon.exercise.bankappevent.domain.ports.AccountActions;
import com.ippon.exercise.bankappevent.domain.ports.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements AccountActions {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

  private AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public Account create(String firstName, String lastName) throws AccountError {
    Account account = new Account(firstName, lastName);
    return accountRepository
        .save(account)
        .orElseThrow(AccountError::new);
  }

  @Override
  public Account getAccount(int id) {
    return accountRepository
        .findById(id)
        .orElseThrow(AccountNotFoundException::new);
  }

  @Override
  public Account deposit(int id, BigDecimal amount) {
    return null;
  }

  @Override
  public Account withdrawal(int id, BigDecimal amount) {
    return null;
  }
}
