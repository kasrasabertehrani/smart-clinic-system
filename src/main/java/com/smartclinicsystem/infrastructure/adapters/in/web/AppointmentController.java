package com.smartclinicsystem.infrastructure.adapters.in.web;

import com.smartclinicsystem.application.port.in.SchedulingUseCase;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.*;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final SchedulingUseCase schedulingUseCase;

    public AppointmentController(SchedulingUseCase schedulingUseCase) {
        this.schedulingUseCase = schedulingUseCase;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> addAppointment(@Valid @RequestBody AddAppointmentCommand command) {
        AppointmentResponse response = schedulingUseCase.addAppointment(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<CancelAppointmentResponse> cancelAppointment(
            @Valid @RequestBody CancelAppointmentCommand command,
            @PathVariable String appointmentId) {
        CancelAppointmentResponse response = schedulingUseCase.cancelAppointment(command, appointmentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{appointmentId}/checkin")
    public ResponseEntity<AppointmentResponse> checkInPatient(@PathVariable String appointmentId) {
        AppointmentResponse response = schedulingUseCase.checkInPatient(appointmentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{appointmentId}/complete")
    public ResponseEntity<AppointmentResponse> completeAppointment(@PathVariable String appointmentId) {
        AppointmentResponse response = schedulingUseCase.completeAppointment(appointmentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{appointmentId}/noshow")
    public ResponseEntity<AppointmentResponse> markAsNoShow(@PathVariable String appointmentId) {
        AppointmentResponse response = schedulingUseCase.markAsNoShow(appointmentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{appointmentId}/reschedule/active")
    public ResponseEntity<RescheduleAppointmentResponse> rescheduleActiveAppointment(
            @Valid @RequestBody RescheduleAppointmentCommand command,
            @PathVariable String appointmentId) {

        RescheduleAppointmentResponse response = schedulingUseCase.rescheduleActiveAppointment(command, appointmentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{appointmentId}/reschedule/system-canceled")
    public ResponseEntity<RescheduleAppointmentResponse> rescheduleSystemCanceledAppointment(
            @Valid @RequestBody RescheduleSystemCanceledCommand command,
            @PathVariable String appointmentId) {

        RescheduleAppointmentResponse response = schedulingUseCase.rescheduleSystemCanceledAppointment(command, appointmentId);
        return ResponseEntity.ok(response);
    }
}