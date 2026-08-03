package com.billingcontext.infrastructure.messaging.appointment.booking;

import com.billingcontext.application.usecase.proforma.IssueProformaCommand;
import com.billingcontext.domain.invoice.AppointmentId;
import com.billingcontext.domain.shared.id.DoctorId;
import com.billingcontext.domain.shared.id.PatientId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AppointmentBookedTranslator {

    private final Clock clock;

    public IssueProformaCommand translate(AppointmentBookedEvent event){
        BigDecimal durationInHours = BigDecimal.valueOf(event.duration())
                .divide(BigDecimal.valueOf(60), 4, java.math.RoundingMode.HALF_UP);

        LocalDateTime utcTime = LocalDateTime.ofInstant(event.appointmentTime(), clock.getZone());

        return new IssueProformaCommand(
                new AppointmentId(event.appointmentId()),
                new DoctorId(event.doctorId()),
                new PatientId(event.patientId()),
                utcTime,
                durationInHours
        );
    }
}
