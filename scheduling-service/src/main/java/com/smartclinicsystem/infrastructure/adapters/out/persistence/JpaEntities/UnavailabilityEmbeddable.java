package com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Embeddable
@EqualsAndHashCode
public class UnavailabilityEmbeddable {

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    protected UnavailabilityEmbeddable() {

    }

    public UnavailabilityEmbeddable(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
