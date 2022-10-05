package com.ippon.exercise.bankappevent.adapter.messaging.event;

import com.ippon.exercise.bankappevent.domain.model.Account;
import java.math.BigDecimal;

public class AccountStatusEvent {

  private int id;
  private BigDecimal balance;
  private String firstName;
  private String lastName;

  public AccountStatusEvent() {
  }

  public AccountStatusEvent(Account account) {
    this.id = account.getId();
    this.firstName = account.getFirstName();
    this.lastName = account.getLastName();
    this.balance = account.getBalance();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
