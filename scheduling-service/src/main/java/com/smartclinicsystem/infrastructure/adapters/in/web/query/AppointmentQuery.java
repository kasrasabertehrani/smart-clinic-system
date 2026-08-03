package com.smartclinicsystem.infrastructure.adapters.in.web.query;

import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.AppointmentResponse;
import com.smartclinicsystem.infrastructure.adapters.out.persistence.dao.AppointmentQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queries/appointments")
@RequiredArgsConstructor
public class AppointmentQuery {

    private final AppointmentQueryDao appointmentQueryDao;

    @GetMapping("/latest")
    public ResponseEntity<AppointmentResponse> getLatestAppointmentByStatus(
            @RequestParam(defaultValue = "SCHEDULED") Appointment.status status) {

        return appointmentQueryDao.getAppointmentStatus(status)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/{appointmentId}/latest")
    public ResponseEntity<AppointmentResponse> getLatestAppointmentById(@PathVariable String appointmentId) {

        return appointmentQueryDao.getAppointmentStatusById(appointmentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}