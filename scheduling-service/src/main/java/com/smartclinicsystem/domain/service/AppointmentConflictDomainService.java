package com.smartclinicsystem.domain.service;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.vo.EffectiveSchedule;
import com.smartclinicsystem.domain.vo.Unavailability;

import java.util.List;
import java.util.stream.Collectors;


public class AppointmentConflictDomainService {

    public List<Appointment> findOverlapsWithUnavailability(
            List<Appointment> candidateAppointments,
            Unavailability newUnavailability) {

        return candidateAppointments.stream()
                .filter(Appointment::isScheduled)
                .filter(app -> newUnavailability.isUnavailableIn(app.getAppointmentTimeSlot()))
                .collect(Collectors.toList());
    }

    public List<Appointment> findConflictsWithNewSchedule(
            List<Appointment> candidateAppointments,
            EffectiveSchedule newSchedule) {

        return candidateAppointments.stream()
                .filter(Appointment::isScheduled)
                .filter(app -> newSchedule.appliesTo(app.getAppointmentTimeSlot().date()))
                .filter(app -> !newSchedule.schedule().isWorkingDuring(app.getAppointmentTimeSlot()))
                .collect(Collectors.toList());
    }
}