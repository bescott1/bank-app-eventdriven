package com.ippon.exercise.bankappevent.adapter.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountStatusEvent;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class AccountAdapter {

  private KafkaService kafkaService;

  private ObjectMapper objectMapper = new ObjectMapper();

  public AccountAdapter(KafkaService kafkaService) {
    this.kafkaService = kafkaService;
  }

}
