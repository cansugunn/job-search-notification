package com.jobsearch.notification.messaging;

import com.jobsearch.notification.data.event.NewJobPostingEvent;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobPostingConsumer {

  private final RabbitTemplate rabbitTemplate;

  @Value("${job-search.rabbitmq.queue}")
  private String queueName;

  /**
   * Pulls all pending messages from the external queue at the moment this is called.
   * Messages remain in the external queue until this method runs — no auto-consumer.
   */
  public List<NewJobPostingEvent> drainQueue() {
    List<NewJobPostingEvent> events = new LinkedList<>();
    while (true) {
      Object rawEvent = rabbitTemplate.receiveAndConvert(queueName);
      if (isNull(rawEvent)) {
        break;
      }

      try {
        NewJobPostingEvent event = (NewJobPostingEvent) rawEvent;
        events.add(event);
        log.info("Pulled from RabbitMQ: jobId={}, title={}", event.jobId(), event.title());
      } catch (Exception e) {
        log.warn("Could not deserialize RabbitMQ message, skipping: {}", e.getMessage());
      }
    }

    log.info("Drained {} message(s) from queue '{}'", events.size(), queueName);
    return events;
  }
}
