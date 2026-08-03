package com.smartclinicsystem.domain.events;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();
}
