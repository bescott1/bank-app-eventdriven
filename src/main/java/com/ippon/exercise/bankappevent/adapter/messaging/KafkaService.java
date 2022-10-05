package com.ippon.exercise.bankappevent.adapter.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountActionEvent;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountStatusEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaService {

  private static final String ACCOUNT_EVENT = "account_event";

  private KafkaTemplate<String, String> kafkaTemplate;
  private ObjectMapper objectMapper = new ObjectMapper();

  public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void publishAccountStatus(AccountStatusEvent message) throws JsonProcessingException {
    kafkaTemplate.send(ACCOUNT_EVENT, objectMapper.writeValueAsString(message));
  }

  @KafkaListener(topics = "account_action")
  public void onAccountActionEvent(String accountEvent) throws JsonProcessingException {
    AccountActionEvent accountActionEvent = objectMapper.readValue(accountEvent,
        AccountActionEvent.class);
    System.out.println(accountActionEvent.toString());

  }
}
