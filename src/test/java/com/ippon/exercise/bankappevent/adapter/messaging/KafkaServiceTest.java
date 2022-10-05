package com.ippon.exercise.bankappevent.adapter.messaging;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ippon.exercise.bankappevent.adapter.messaging.event.AccountStatusEvent;
import com.ippon.exercise.bankappevent.domain.model.Account;
import com.ippon.exercise.bankappevent.domain.ports.AccountActions;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class KafkaServiceTest {

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @Mock
  private AccountActions accountActions;

  @InjectMocks
  public KafkaService subject;

  @Test
  public void testPublishingSerialization() throws JsonProcessingException {
    //Given
    AccountStatusEvent accountStatusEvent = new AccountStatusEvent();
    accountStatusEvent.setEventId("testId");
    accountStatusEvent.setFirstName("Ben");
    accountStatusEvent.setLastName("Scott");
    accountStatusEvent.setBalance(BigDecimal.TEN);
    accountStatusEvent.setId(6);

    //When
    subject.publishAccountStatus(accountStatusEvent);

    //Then
    Mockito.verify(kafkaTemplate).send("account_event", "{\"eventId\":\"testId\","
        + "\"id\":6,"
        + "\"balance\":10,"
        + "\"firstName\":\"Ben\","
        + "\"lastName\":\"Scott\"}");
  }

  @Test
  public void testOnAccountActionEvent() throws JsonProcessingException {
    //Given
    Account account = new Account("Ben", "Scott");
    account.setId(3);
    account.setBalance(BigDecimal.valueOf(13.32));
    Mockito.when(accountActions.getAccount(3)).thenReturn(account);

    //When
    subject.onAccountActionEvent("{\"action\":\"get\",\"id\":\"3\", \"eventId\":\"testId\"}");

    //Then
    Mockito.verify(kafkaTemplate).send("account_event", "{\"eventId\":\"testId\","
        + "\"id\":3,"
        + "\"balance\":13.32,"
        + "\"firstName\":\"Ben\","
        + "\"lastName\":\"Scott\"}");
  }

}