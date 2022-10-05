package com.ippon.exercise.bankappevent.domain.ports;

import com.ippon.exercise.bankappevent.domain.exception.AccountError;
import com.ippon.exercise.bankappevent.domain.model.Account;
import java.math.BigDecimal;

public interface AccountActions {

  Account create(String firstName, String lastName) throws AccountError;

  Account getAccount(int id);

  Account deposit(int id, BigDecimal amount);

  Account withdrawal(int id, BigDecimal amount);

}
