package com.smartclinicsystem.infrastructure.adapters.in.web;

import com.smartclinicsystem.application.command.AddAppointmentCommand;
import com.smartclinicsystem.application.command.CancelAppointmentCommand;
import com.smartclinicsystem.application.command.RescheduleAppointmentCommand;
import com.smartclinicsystem.application.command.RescheduleSystemCanceledCommand;
import com.smartclinicsystem.application.port.in.SchedulingUseCase;
import com.smartclinicsystem.domain.vo.AppointmentId;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.*;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.*;
import com.smartclinicsystem.infrastructure.adapters.in.web.translator.RequestToCommandTranslator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final SchedulingUseCase schedulingUseCase;
    private final RequestToCommandTranslator translator;


    @PostMapping
    public ResponseEntity<AppointmentResponse> addAppointment(@Valid @RequestBody AddAppointmentRequest request) {

        AddAppointmentCommand command = translator.translateToAddAppointment(request);
        AppointmentResponse response = schedulingUseCase.addAppointment(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<CancelAppointmentResponse> cancelAppointment(
            @Valid @RequestBody CancelAppointmentRequest request,
            @PathVariable String appointmentId) {
        CancelAppointmentCommand command = translator.translateToCancelAppointment(request, appointmentId);
        CancelAppointmentResponse response = schedulingUseCase.cancelAppointment(command);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{appointmentId}/checkin")
    public ResponseEntity<AppointmentResponse> checkInPatient(@PathVariable String appointmentId) {
        AppointmentResponse response = schedulingUseCase.checkInPatient(new AppointmentId(appointmentId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{appointmentId}/complete")
    public ResponseEntity<AppointmentResponse> completeAppointment(@PathVariable String appointmentId) {
        AppointmentResponse response = schedulingUseCase.completeAppointment(new AppointmentId(appointmentId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{appointmentId}/noshow")
    public ResponseEntity<AppointmentResponse> markAsNoShow(@PathVariable String appointmentId) {
        AppointmentResponse response = schedulingUseCase.markAsNoShow(new AppointmentId(appointmentId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{appointmentId}/reschedule/active")
    public ResponseEntity<RescheduleAppointmentResponse> rescheduleActiveAppointment(
            @Valid @RequestBody RescheduleAppointmentRequest request,
            @PathVariable String appointmentId) {
        RescheduleAppointmentCommand command = translator.translateToRescheduleAppointment(request, appointmentId);
        RescheduleAppointmentResponse response = schedulingUseCase.rescheduleActiveAppointment(command);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{appointmentId}/reschedule/system-canceled")
    public ResponseEntity<RescheduleAppointmentResponse> rescheduleSystemCanceledAppointment(
            @Valid @RequestBody RescheduleSystemCanceledRequest request,
            @PathVariable String appointmentId) {
        RescheduleSystemCanceledCommand command = translator.translateToRescheduleSystemCanceled(request, appointmentId);
        RescheduleAppointmentResponse response = schedulingUseCase.rescheduleSystemCanceledAppointment(command);
        return ResponseEntity.ok(response);
    }
}