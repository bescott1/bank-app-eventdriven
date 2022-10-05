package com.ippon.exercise.bankappevent.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountActionEvent;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountStatusEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class KafkaTestConsumer {

  private ObjectMapper objectMapper = new ObjectMapper();

  private Map<String, CountDownLatch> countDownLatchHashMap = new HashMap<>();
  private Map<String, AccountStatusEvent> responseMap = new HashMap<>();

  @KafkaListener(topics = "account_event")
  public void onAccountActionEvent(String accountEvent) throws IOException {

    AccountStatusEvent accountStatusEvent = objectMapper.readValue(accountEvent,
        AccountStatusEvent.class);
    responseMap.put(accountStatusEvent.getEventId(), accountStatusEvent);
    CountDownLatch countDownLatch = countDownLatchHashMap.get(accountStatusEvent.getEventId());
    if (countDownLatch != null) {
      countDownLatch.countDown();
    }
  }

  public Optional<AccountStatusEvent> waitOnResponse(String eventId) throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    countDownLatchHashMap.put(eventId, countDownLatch);
    countDownLatch.await(50, TimeUnit.SECONDS);
    return Optional.ofNullable(responseMap.get(eventId));
  }
}
