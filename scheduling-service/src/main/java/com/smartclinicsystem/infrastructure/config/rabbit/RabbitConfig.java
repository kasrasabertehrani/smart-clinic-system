package com.smartclinicsystem.infrastructure.config.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RabbitConfig {

    @Value("${app.messaging.exchanges.appointment}")
    private String appointmentExchangeName;

    @Value("${app.messaging.queues.appointment-expired}")
    private String expiredQueueName;

    @Value("${app.messaging.routing-keys.appointment-expired}")
    private String expiredRoutingKey;

    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(appointmentExchangeName);
    }

    @Bean
    public Queue expiredQueue() {
        return new Queue(expiredQueueName, true);
    }


    @Bean
    public Binding expiredBinding(Queue expiredQueue, TopicExchange appointmentExchange) {
        return BindingBuilder.bind(expiredQueue)
                .to(appointmentExchange)
                .with(expiredRoutingKey);
    }

    @Bean
    @SuppressWarnings("removal")
    public MessageConverter jsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return new org.springframework.amqp.support.converter.Jackson2JsonMessageConverter(mapper);
    }
}