package com.smartclinicsystem.infrastructure.adapters.out.persistence.JpaEntities;



import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "appointment_calendars")
public class AppointmentCalendarEntity {

    @Id
    @Column(name = "doctor_id")
    private String doctorId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "calendar_unavailabilities",
            joinColumns = @JoinColumn(name = "calendar_doctor_id")
    )
    private List<UnavailabilityEmbeddable> unavailabilities = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "calendar_effective_schedules",
            joinColumns = @JoinColumn(name = "calendar_doctor_id")
    )
    private List<EffectiveScheduleEmbeddable> effectiveSchedules = new ArrayList<>();

    protected AppointmentCalendarEntity() {

    }

    public AppointmentCalendarEntity(String doctorId) {
        this.doctorId = doctorId;
    }



    public void addUnavailability(UnavailabilityEmbeddable unavailability) {
        this.unavailabilities.add(unavailability);
    }

    public void addEffectiveSchedule(EffectiveScheduleEmbeddable schedule) {
        this.effectiveSchedules.add(schedule);
    }
}