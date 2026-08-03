package com.billingcontext.infrastructure.messaging.appointment.expire;

import com.billingcontext.application.DomainEventPublisher;
import com.billingcontext.domain.shared.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceExpirationEventPublisher implements DomainEventPublisher {

    private final RabbitTemplate rabbitTemplate;


    @Value("${app.messaging.exchanges.appointment}")
    private String appointmentExchange;

    @Value("${app.messaging.routing-keys.appointment-expired}")
    private String expiredRoutingKey;

    public void publish(DomainEvent event){
        rabbitTemplate.convertAndSend(appointmentExchange, expiredRoutingKey, event);
    }
}
