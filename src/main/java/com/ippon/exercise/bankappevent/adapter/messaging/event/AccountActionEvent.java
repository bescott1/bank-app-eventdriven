package com.ippon.exercise.bankappevent.adapter.messaging.event;

import java.math.BigDecimal;

public class AccountActionEvent {

  private String action;
  private String firstName;
  private String lastName;

  private BigDecimal balance = BigDecimal.ZERO;

  public AccountActionEvent() {
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
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

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  @Override
  public String toString() {
    return "AccountActionEvent{" +
        "action='" + action + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", balance=" + balance +
        '}';
  }
}
