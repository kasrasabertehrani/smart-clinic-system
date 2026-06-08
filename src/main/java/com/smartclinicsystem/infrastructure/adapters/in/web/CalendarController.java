package com.smartclinicsystem.infrastructure.adapters.in.web;

import com.smartclinicsystem.application.port.in.CalendarUseCase;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.request.*;
import com.smartclinicsystem.infrastructure.adapters.in.web.DTO.response.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendars")
public class CalendarController {

    private final CalendarUseCase calendarUseCase;

    public CalendarController(CalendarUseCase calendarUseCase) {
        this.calendarUseCase = calendarUseCase;
    }


    @PostMapping("/{doctorId}/unavailabilities")
    public ResponseEntity<AddUnavailabilityResponse> addUnavailability(
            @Valid @RequestBody AddUnavailabilityCommand command,
            @PathVariable String doctorId) {

        AddUnavailabilityResponse response = calendarUseCase.addUnavailability(command, doctorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/{doctorId}/schedules")
    public ResponseEntity<ChangeScheduleResponse> changeSchedule(
            @Valid @RequestBody ChangeScheduleCommand command,
            @PathVariable String doctorId) {

        ChangeScheduleResponse response = calendarUseCase.changeSchedule(command, doctorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}