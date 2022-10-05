package com.ippon.exercise.bankappevent.adapter.postgres;

import com.ippon.exercise.bankappevent.domain.model.Account;
import com.ippon.exercise.bankappevent.domain.ports.AccountRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AccountRepositoryAdapter implements AccountRepository {

  @Override
  public Optional<Account> save(Account account) {
    return Optional.empty();
  }

  @Override
  public List<Account> findAll() {
    return null;
  }

  @Override
  public Optional<Account> findById(int id) {
    return Optional.empty();
  }
}
