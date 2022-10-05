package com.ippon.exercise.bankappevent.adapter.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountActionEvent;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountStatusEvent;
import com.ippon.exercise.bankappevent.domain.model.Account;
import com.ippon.exercise.bankappevent.domain.ports.AccountActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaService {
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaService.class);

  private static final String ACCOUNT_EVENT = "account_event";
  private static final String ACCOUNT_ACTION = "account_action";

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final AccountActions accountActions;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public KafkaService(KafkaTemplate<String, String> kafkaTemplate, AccountActions accountActions) {
    this.kafkaTemplate = kafkaTemplate;
    this.accountActions = accountActions;
  }

  public void publishAccountStatus(AccountStatusEvent message) throws JsonProcessingException {
    kafkaTemplate.send(ACCOUNT_EVENT, objectMapper.writeValueAsString(message));
  }

  public void publishAccountAction(AccountActionEvent message) throws JsonProcessingException {
    kafkaTemplate.send(ACCOUNT_ACTION, objectMapper.writeValueAsString(message));

  }

  @KafkaListener(topics = "account_action")
  public void onAccountActionEvent(String accountEvent) throws JsonProcessingException {
    LOGGER.info("Received event: {}", accountEvent);

    AccountActionEvent accountActionEvent = objectMapper.readValue(accountEvent,
        AccountActionEvent.class);

    switch (accountActionEvent.getAction()) {
      case "create":
        createAccount(accountActionEvent);
        break;
      case "get":
        getAccount(accountActionEvent);
        break;
      default:
        LOGGER.error("Unhandled command");
    }

  }

  private void createAccount(AccountActionEvent accountActionEvent) throws JsonProcessingException {
    Account account = accountActions.create(accountActionEvent.getFirstName(),
        accountActionEvent.getLastName());
    publishAccountStatus(new AccountStatusEvent(account, accountActionEvent.getEventId()));
  }

  private void getAccount(AccountActionEvent accountActionEvent) throws JsonProcessingException {
    Account account = accountActions.getAccount(accountActionEvent.getId());
    publishAccountStatus(new AccountStatusEvent(account, accountActionEvent.getEventId()));
  }
}
