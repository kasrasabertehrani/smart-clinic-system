package com.smartclinicsystem.infrastructure.adapters.out.messaging.out;



import com.smartclinicsystem.application.port.out.DomainEventPublisher;
import com.smartclinicsystem.domain.events.AppointmentWasBooked;
import com.smartclinicsystem.domain.events.AppointmentWasCanceled;
import com.smartclinicsystem.domain.events.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisherAdapter implements DomainEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.messaging.exchanges.appointment}")
    private String appointmentExchange;

    @Value("${app.messaging.routing-keys.appointment-booked}")
    private String bookedRoutingKey;

    @Value("${app.messaging.routing-keys.appointment-canceled}")
    private String canceledRoutingKey;

    @Override
    public void publish(DomainEvent event) {

        if (event instanceof AppointmentWasBooked bookedEvent) {
            rabbitTemplate.convertAndSend(
                    appointmentExchange,
                    bookedRoutingKey,
                    bookedEvent
            );
        } else if (event instanceof AppointmentWasCanceled canceledEvent) {
            rabbitTemplate.convertAndSend(
                    appointmentExchange,
                    canceledRoutingKey,
                    canceledEvent
            );
        } else {
            throw new IllegalArgumentException("Unknown event type: " + event.getClass());
        }
    }
}
