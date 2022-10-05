package com.ippon.exercise.bankappevent.adapter.postgres;

import com.ippon.exercise.bankappevent.domain.exception.AccountNotFoundException;
import com.ippon.exercise.bankappevent.domain.model.Account;
import com.ippon.exercise.bankappevent.domain.ports.AccountRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AccountRepositoryAdapter implements AccountRepository {

  private AccountPostgresRepository repository;

  public AccountRepositoryAdapter(AccountPostgresRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<Account> save(Account account) {
    AccountDAO saveObject = new AccountDAO(account.getFirstName(), account.getLastName());
    saveObject.setBalance(account.getBalance());
    AccountDAO save = repository.save(saveObject);
    account.setId(save.getId());
    return Optional.of(account);
  }

  @Override
  public Optional<Account> findById(int id) {
    try {
      AccountDAO accountDAO = repository
          .findById(id)
          .orElseThrow(AccountNotFoundException::new);
      return Optional.of(accountDaoToAccount(accountDAO));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private Account accountDaoToAccount(AccountDAO accountDAO) {
    Account account = new Account(accountDAO.getFirstName(), accountDAO.getLastName());
    account.setId(accountDAO.getId());
    account.setBalance(accountDAO.getBalance());
    return account;
  }
}
