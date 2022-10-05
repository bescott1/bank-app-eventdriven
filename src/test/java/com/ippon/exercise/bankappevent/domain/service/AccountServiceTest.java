package com.ippon.exercise.bankappevent.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ippon.exercise.bankappevent.domain.exception.AccountNotFoundException;
import com.ippon.exercise.bankappevent.domain.model.Account;
import com.ippon.exercise.bankappevent.domain.ports.AccountRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  private AccountRepository accountRepository;

  @InjectMocks
  public AccountService subject;

  @Test
  public void testCreateAccount() {
    //given
    Account account = new Account("Ben", "Scott");

    Account expectedAccount =  new Account("Ben", "Scott");
    expectedAccount.setBalance(BigDecimal.TEN);
    expectedAccount.setId(3);
    Mockito.when(accountRepository.save(account)).thenReturn(Optional.of(expectedAccount));

    //when
    Account actual = subject.create("Ben", "Scott");

    //then
    Assertions.assertEquals(actual.getFirstName(), "Ben");
    Assertions.assertEquals(actual.getLastName(), "Scott");
    Assertions.assertEquals(actual.getBalance(), BigDecimal.TEN);
    Assertions.assertEquals(actual.getId(), 3);
  }

  @Test
  public void testGetAccount() {
    //given
    Account expectedAccount =  new Account("Ben", "Scott");
    expectedAccount.setBalance(BigDecimal.TEN);
    expectedAccount.setId(2);
    Mockito.when(accountRepository.findById(2)).thenReturn(Optional.of(expectedAccount));
    //when
    Account actual = subject.getAccount(2);

    //then
    Assertions.assertEquals(actual.getFirstName(), "Ben");
    Assertions.assertEquals(actual.getLastName(), "Scott");
    Assertions.assertEquals(actual.getBalance(), BigDecimal.TEN);
    Assertions.assertEquals(actual.getId(), 2);
  }

  @Test
  public void testAccountNotFound() {
    //given
    Mockito.when(accountRepository.findById(3)).thenReturn(Optional.empty());
    //when
    assertThrows(AccountNotFoundException.class, () -> subject.getAccount(3));
  }

}