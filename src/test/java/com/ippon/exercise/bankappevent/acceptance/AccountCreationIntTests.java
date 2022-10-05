package com.ippon.exercise.bankappevent.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ippon.exercise.bankappevent.adapter.messaging.KafkaService;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountActionEvent;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountStatusEvent;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

//@Testcontainers
@SpringBootTest
public class AccountCreationIntTests {

//  @Container
  public static KafkaContainer kafka =
      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

  @Autowired
  private KafkaService kafkaService;

  @Autowired
  private KafkaTestConsumer kafkaTestConsumer;

  @Test
  public void createAccountEndToEnd() throws JsonProcessingException, InterruptedException {

    AccountActionEvent accountActionEvent = new AccountActionEvent();
    accountActionEvent.setEventId("TEST-CREATE-" + UUID.randomUUID().toString().substring(0,8));
    accountActionEvent.setAction("create");
    accountActionEvent.setFirstName("Ben");
    accountActionEvent.setLastName("Scott");

    kafkaService.publishAccountAction(accountActionEvent);

    Optional<AccountStatusEvent> accountStatusEvent = kafkaTestConsumer.waitOnResponse(
        accountActionEvent.getEventId());
    Assertions.assertTrue(accountStatusEvent.isPresent());

    AccountStatusEvent createResult = accountStatusEvent.get();

    AccountActionEvent getAction = new AccountActionEvent();
    getAction.setEventId("TEST-CREATE-" + UUID.randomUUID().toString().substring(0,8));
    getAction.setAction("get");
    getAction.setId(createResult.getId());

    kafkaService.publishAccountAction(getAction);

    Optional<AccountStatusEvent> retrieveAccount = kafkaTestConsumer.waitOnResponse(
        getAction.getEventId());
    Assertions.assertTrue(retrieveAccount.isPresent());
    AccountStatusEvent getActionResult = accountStatusEvent.get();

    Assertions.assertEquals(accountActionEvent.getFirstName(), getActionResult.getFirstName());
    Assertions.assertEquals(accountActionEvent.getLastName(), getActionResult.getLastName());
    Assertions.assertEquals(BigDecimal.ZERO, getActionResult.getBalance());
    Assertions.assertEquals(createResult.getId(), getActionResult.getId());
  }


}
