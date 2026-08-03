package com.billingcontext.infrastructure.messaging.appointment.cancellation;

import com.billingcontext.application.usecase.cancellation.InvoiceCancellationCommand;
import com.billingcontext.domain.invoice.AppointmentId;
import com.billingcontext.domain.policy.refund.evaluation.CancellationInitiator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.time.Duration;


@Component
@RequiredArgsConstructor
public class AppointmentCancelledTranslator {

    public InvoiceCancellationCommand translate(AppointmentCancelledEvent event) {
        return new InvoiceCancellationCommand(
                CancellationInitiator.valueOf(event.cancelInitiator().toUpperCase()),

                Duration.between(event.bookingTime(), event.cancellationTime()).toMinutes(),
                Duration.between(event.cancellationTime(), event.appointmentTime()).toMinutes(),

                new AppointmentId(event.appointmentId())
        );
    }
}
