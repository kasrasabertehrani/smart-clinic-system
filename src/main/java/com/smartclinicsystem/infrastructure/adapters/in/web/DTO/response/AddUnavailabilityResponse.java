package com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.vo.Unavailability;

import java.time.LocalDateTime;
import java.util.List;

public record AddUnavailabilityResponse(
        LocalDateTime unavailabilityStart,
        LocalDateTime unavailabilityEnd,
        List<CancelAppointmentResponse> canceledAppointments) {

    public static AddUnavailabilityResponse from(
            Unavailability unavailability,
            List<Appointment> canceledAppointments) {


        List<CancelAppointmentResponse> canceledList = canceledAppointments.stream()
                .map(CancelAppointmentResponse::from)
                .toList();

        return new AddUnavailabilityResponse(
                unavailability.unavailabilityPeriod().startTime(),
                unavailability.unavailabilityPeriod().endTime(),
                canceledList
        );
    }
}