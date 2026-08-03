package com.billingcontext.application;

import com.billingcontext.domain.shared.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
