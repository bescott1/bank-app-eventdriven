package com.ippon.exercise.bankappevent.adapter.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountActionEvent;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountStatusEvent;
import com.ippon.exercise.bankappevent.domain.model.Account;
import com.ippon.exercise.bankappevent.domain.ports.AccountActions;
import com.ippon.exercise.bankappevent.domain.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaService {
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaService.class);

  private static final String ACCOUNT_EVENT = "account_event";

  private KafkaTemplate<String, String> kafkaTemplate;
  private AccountActions accountActions;
  private ObjectMapper objectMapper = new ObjectMapper();

  public KafkaService(KafkaTemplate<String, String> kafkaTemplate, AccountActions accountActions) {
    this.kafkaTemplate = kafkaTemplate;
    this.accountActions = accountActions;
  }

  public void publishAccountStatus(AccountStatusEvent message) throws JsonProcessingException {
    kafkaTemplate.send(ACCOUNT_EVENT, objectMapper.writeValueAsString(message));
  }

  @KafkaListener(topics = "account_action")
  public void onAccountActionEvent(String accountEvent) throws JsonProcessingException {
    AccountActionEvent accountActionEvent = objectMapper.readValue(accountEvent,
        AccountActionEvent.class);

    LOGGER.info("Received event: {}", accountActionEvent);
    switch (accountActionEvent.getAction()) {
      case "create":
        Account account = accountActions.create(accountActionEvent.getFirstName(),
            accountActionEvent.getLastName());
        publishAccountStatus(new AccountStatusEvent(account));
        break;
      default:
        LOGGER.error("Unhandled command");

    }

  }
}
