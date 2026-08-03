package com.billingcontext.infrastructure.config.rabbitmq;

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

    @Value("${app.messaging.queues.appointment-booked}")
    private String bookedQueueName;

    @Value("${app.messaging.queues.appointment-canceled}")
    private String canceledQueueName;

    @Value("${app.messaging.exchanges.appointment}")
    private String appointmentExchangeName;

    @Value("${app.messaging.routing-keys.appointment-booked}")
    private String bookedRoutingKey;

    @Value("${app.messaging.routing-keys.appointment-canceled}")
    private String canceledRoutingKey;

    @Bean
    public Queue bookedQueue() {
        return new Queue(bookedQueueName, true);
    }

    @Bean
    public Queue canceledQueue() {
        return new Queue(canceledQueueName, true);
    }

    @Bean
    public TopicExchange appointmentExchange() {
        return new TopicExchange(appointmentExchangeName);
    }

    @Bean
    public Binding bookedBinding(Queue bookedQueue, TopicExchange appointmentExchange) {
        return BindingBuilder.bind(bookedQueue).to(appointmentExchange).with(bookedRoutingKey);
    }

    @Bean
    public Binding canceledBinding(Queue canceledQueue, TopicExchange appointmentExchange) {
        return BindingBuilder.bind(canceledQueue).to(appointmentExchange).with(canceledRoutingKey);
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