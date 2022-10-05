package com.ippon.exercise.bankappevent.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ippon.exercise.bankappevent.adapter.messaging.KafkaService;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountActionEvent;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountStatusEvent;
import com.ippon.exercise.bankappevent.domain.service.AccountService;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountDepositIntTests {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccountDepositIntTests.class);

  @Autowired
  private KafkaService kafkaService;

  @Autowired
  private KafkaTestConsumer kafkaTestConsumer;

  @Test
  public void depositIntoAnAccountTest() throws JsonProcessingException, InterruptedException {
    String testCorrelationId = UUID.randomUUID().toString().substring(0,5);
    LOGGER.info("Test correlationID: {}", testCorrelationId);
    AccountStatusEvent accountStatusEvent = createAccount("Ben", "Scott", testCorrelationId);
    AccountStatusEvent depositStatusEvent = depositIntoAccount(accountStatusEvent.getId(), 32.14, testCorrelationId);
    AccountStatusEvent getActionResult = retrieveAccount(depositStatusEvent.getId(), testCorrelationId);

    Assertions.assertEquals("Ben", getActionResult.getFirstName());
    Assertions.assertEquals("Scott", getActionResult.getLastName());
    Assertions.assertEquals(BigDecimal.valueOf(32.14).setScale(2), getActionResult.getBalance());
    Assertions.assertEquals(accountStatusEvent.getId(), getActionResult.getId());
  }

  private AccountStatusEvent depositIntoAccount(int accountId, double amount, String testCorrelationId)
      throws JsonProcessingException, InterruptedException {

    AccountActionEvent depositAction = new AccountActionEvent();
    depositAction.setEventId("TEST-DEPOSIT-" + testCorrelationId + UUID.randomUUID().toString().substring(0,8));
    depositAction.setAction("deposit");
    depositAction.setAmount(BigDecimal.valueOf(amount));
    depositAction.setId(accountId);

    kafkaService.publishAccountAction(depositAction);
    Optional<AccountStatusEvent> depositStatusEvent = kafkaTestConsumer.waitOnResponse(
        depositAction.getEventId());
    Assertions.assertTrue(depositStatusEvent.isPresent());

    return depositStatusEvent.get();
  }

  private AccountStatusEvent retrieveAccount(int accountId, String testCorrelationId)
      throws JsonProcessingException, InterruptedException {
    AccountActionEvent getAction = new AccountActionEvent();
    getAction.setEventId("TEST-CREATE-" + testCorrelationId+ UUID.randomUUID().toString().substring(0,8));
    getAction.setAction("get");
    getAction.setId(accountId);

    kafkaService.publishAccountAction(getAction);

    Optional<AccountStatusEvent> retrieveAccount = kafkaTestConsumer.waitOnResponse(
        getAction.getEventId());
    Assertions.assertTrue(retrieveAccount.isPresent());
    return retrieveAccount.get();
  }

  private AccountStatusEvent createAccount(String firstName, String lastName, String testCorrelationId)
      throws JsonProcessingException, InterruptedException {
    AccountActionEvent accountActionEvent = new AccountActionEvent();
    accountActionEvent.setEventId("TEST-CREATE-" + testCorrelationId + UUID.randomUUID().toString().substring(0,8));
    accountActionEvent.setAction("create");
    accountActionEvent.setFirstName(firstName);
    accountActionEvent.setLastName(lastName);

    kafkaService.publishAccountAction(accountActionEvent);

    Optional<AccountStatusEvent> accountStatusEvent = kafkaTestConsumer.waitOnResponse(
        accountActionEvent.getEventId());
    Assertions.assertTrue(accountStatusEvent.isPresent());
    return accountStatusEvent.get();
  }


}
