package com.smartclinicsystem.infrastructure.adapters.in.web;

import com.smartclinicsystem.application.port.in.SchedulingUseCase;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.AddAppointmentCommand;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.CancelAppointmentCommand;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.RescheduleAppointmentCommand;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.RescheduleSystemCanceledCommand;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.AppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.CancelAppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.RescheduleAppointmentResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    private final SchedulingUseCase schedulingUseCase;

    public AppointmentController(SchedulingUseCase schedulingUseCase) {
        this.schedulingUseCase = schedulingUseCase;
    }
    @PostMapping("/add")
    public ResponseEntity<AppointmentResponse> addAppointment(@Valid @RequestBody AddAppointmentCommand command) {
        AppointmentResponse response = schedulingUseCase.addAppointment(command);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/cancel/{appointmentId}")
    public ResponseEntity<CancelAppointmentResponse> cancelAppointment(@Valid @RequestBody CancelAppointmentCommand command,
                                                                @PathVariable String appointmentId){
        CancelAppointmentResponse response = schedulingUseCase.cancelAppointment(command, appointmentId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/checkin/{appointmentId}")
    public ResponseEntity<AppointmentResponse> checkInPatient(@PathVariable String appointmentId) {
        AppointmentResponse response = schedulingUseCase.checkInPatient(appointmentId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("complete/{appointmentId}")
    public ResponseEntity<AppointmentResponse> completeAppointment(@PathVariable String appointmentId) {
        AppointmentResponse response = schedulingUseCase.completeAppointment(appointmentId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("noshow/{appointmentId}")
    public ResponseEntity<AppointmentResponse> markAsNoShow(@PathVariable String appointmentId) {
        AppointmentResponse response = schedulingUseCase.markAsNoShow(appointmentId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("reschedule/active/{appointmentId}")
    public ResponseEntity<RescheduleAppointmentResponse> rescheduleActiveAppointment(
       @Valid @RequestBody RescheduleAppointmentCommand command, @PathVariable String appointmentId) {
        RescheduleAppointmentResponse response = schedulingUseCase.rescheduleActiveAppointment(command, appointmentId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("reschedule/system-canceled/{appointmentId}")
    public ResponseEntity<RescheduleAppointmentResponse> rescheduleSystemCanceledAppointment(
            @Valid @RequestBody RescheduleSystemCanceledCommand command, @PathVariable String appointmentId) {

        RescheduleAppointmentResponse response = schedulingUseCase.rescheduleSystemCanceledAppointment(command, appointmentId);
        return ResponseEntity.ok(response);
    }
}
