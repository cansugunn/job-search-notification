package com.jobsearch.notification.configuration;

import com.jobsearch.notification.data.event.NewJobPostingEvent;
import java.util.Map;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  @Value("${job-search.rabbitmq.queue}")
  private String queueName;

  @Bean
  public Queue newJobPostingsQueue() {
    return QueueBuilder.durable(queueName).build();
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
    typeMapper.setTrustedPackages("*");
    typeMapper.setIdClassMapping(Map.of(
        "com.jobsearch.data.event.NewJobPostingEvent", NewJobPostingEvent.class));

    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    converter.setJavaTypeMapper(typeMapper);
    return converter;
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter());
    return template;
  }
}
