package com.smartclinicsystem.infrastructure.adapters.in.web;

import com.smartclinicsystem.application.command.AddUnavailabilityCommand;
import com.smartclinicsystem.application.command.ChangeScheduleCommand;
import com.smartclinicsystem.application.port.in.CalendarUseCase;
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
@RequestMapping("/api/calendars")
public class CalendarController {

    private final CalendarUseCase calendarUseCase;
    private final RequestToCommandTranslator translator;


    @PostMapping("/{doctorId}/unavailabilities")
    public ResponseEntity<AddUnavailabilityResponse> addUnavailability(
            @Valid @RequestBody AddUnavailabilityRequest request,
            @PathVariable String doctorId) {

        AddUnavailabilityCommand command = translator.translateToAddUnavailability(request, doctorId);
        AddUnavailabilityResponse response = calendarUseCase.addUnavailability(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/{doctorId}/schedules")
    public ResponseEntity<ChangeScheduleResponse> changeSchedule(
            @Valid @RequestBody ChangeScheduleRequest request,
            @PathVariable String doctorId) {
        ChangeScheduleCommand command = translator.translateToChangeSchedule(request, doctorId);
        ChangeScheduleResponse response = calendarUseCase.changeSchedule(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}