package com.smartclinicsystem.application.port.out;

import com.smartclinicsystem.domain.events.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
