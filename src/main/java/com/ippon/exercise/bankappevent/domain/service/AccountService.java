package com.ippon.exercise.bankappevent.domain.service;

import com.ippon.exercise.bankappevent.domain.exception.AccountNotFoundException;
import com.ippon.exercise.bankappevent.domain.model.Account;
import com.ippon.exercise.bankappevent.domain.ports.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
  private static final Logger log = LoggerFactory.getLogger(AccountService.class);

  private AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public Account createAccount(Account newAccount) throws Exception {
    Account account = new Account(newAccount.getFirstName(), newAccount.getLastName());
    return accountRepository
        .save(account)
        .orElseThrow(Exception::new);
  }

  public Account getAccount(Integer id) {
    return accountRepository
        .findById(id)
        .orElseThrow(AccountNotFoundException::new);
  }

  public List<Account> getAllAccounts() {
    return accountRepository.findAll();
  }


}
