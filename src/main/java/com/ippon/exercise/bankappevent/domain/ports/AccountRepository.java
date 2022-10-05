package com.ippon.exercise.bankappevent.domain.ports;


import com.ippon.exercise.bankappevent.domain.model.Account;
import java.util.Optional;

public interface AccountRepository  {

    Optional<Account> save(Account account);
    Optional<Account> findById(int id);
}
