package com.ippon.exercise.bankappevent.domain.ports;


import com.ippon.exercise.bankappevent.domain.model.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository  {

    Optional<Account> save(Account account);
    Optional<Account> findById(int id);
}
