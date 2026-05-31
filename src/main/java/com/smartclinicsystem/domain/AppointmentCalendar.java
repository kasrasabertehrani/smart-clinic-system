package com.smartclinicsystem.domain;

import com.smartclinicsystem.domain.exception.*;
import com.smartclinicsystem.domain.vo.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class AppointmentCalendar {
    private final List<EffectiveSchedule> effectiveSchedules;
    private final List<Unavailability> unavailabilities;
    private final DoctorId doctorId;

    public AppointmentCalendar(
            DoctorId doctorId,
            List<EffectiveSchedule> effectiveSchedules,
            List<Unavailability> unavailabilities
            ) {

        this.doctorId = doctorId;
        this.effectiveSchedules = new ArrayList<>(effectiveSchedules);
        this.unavailabilities = new ArrayList<>(unavailabilities);

    }



    private boolean isDoctorAvailable(TimeSlot requestedTime) {
        return this.unavailabilities.stream()
                .noneMatch(unavail -> unavail.isUnavailableIn(requestedTime));
    }

    private boolean isAlreadyBooked(TimeSlot requestedTimeSlot, List<TimeSlot> alreadyBookedSlots) {
        return alreadyBookedSlots.stream()
                .anyMatch(bookedSlot -> bookedSlot.overlapsWith(requestedTimeSlot));
    }

    private boolean isDoctorWorking(TimeSlot requestedPeriod) {
        LocalDate requestedDate = requestedPeriod.date();

        EffectiveSchedule activeSchedule = this.effectiveSchedules.stream()
                .filter(schedule -> schedule.appliesTo(requestedDate))
                .max(Comparator.comparing(EffectiveSchedule::validFrom))
                .orElseThrow(() -> new InvalidEffectiveScheduleException(
                        "Cannot process request: No valid working schedule found for the date " + requestedDate
                ));

        return activeSchedule.schedule().isWorkingDuring(requestedPeriod);
    }

    public Unavailability addUnavailability(TimePeriod leavePeriod) {
        Unavailability newUnavailability = new Unavailability(leavePeriod);
        this.unavailabilities.add(newUnavailability);
        return newUnavailability;
    }

    public EffectiveSchedule changeSchedule(WeeklySchedule newSchedule, LocalDate effectiveDate) {
        boolean dateAlreadyExists = this.effectiveSchedules.stream()
                .anyMatch(existing -> existing.validFrom().equals(effectiveDate));

        if (dateAlreadyExists) {
            throw new InvalidEffectiveScheduleException(
                    "A schedule already exists starting on " + effectiveDate +
                            ". You must edit the existing schedule or choose a different start date."
            );
        }

        EffectiveSchedule newEffectiveSchedule = new EffectiveSchedule(effectiveDate, newSchedule);
        this.effectiveSchedules.add(newEffectiveSchedule);
        return newEffectiveSchedule;
    }



    public void validateBookingRules(TimeSlot requestedTimeSlot, List<TimeSlot> alreadyBookedSlots) {
        if (!isDoctorWorking(requestedTimeSlot)) {
            throw new BookingException("Cannot book: The requested time falls outside working hours.");
        }
        if (!isDoctorAvailable(requestedTimeSlot)) {
            throw new BookingException("Cannot book: The doctor is on leave during this time.");
        }
        if (isAlreadyBooked(requestedTimeSlot,alreadyBookedSlots)) {
            throw new BookingException("Cannot book: This time slot is already taken.");
        }
    }

}
