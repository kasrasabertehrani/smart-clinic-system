package com.billingcontext.infrastructure.messaging.appointment.booking;

import com.billingcontext.application.usecase.proforma.IssueProformaCommand;
import com.billingcontext.application.usecase.proforma.ProformaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentBookedEventListener {

    private final ProformaUseCase proformaUseCase;
    private final AppointmentBookedTranslator translator;

    @RabbitListener(queues = "${app.messaging.queues.appointment-booked}")
    public void onAppointmentBooked(AppointmentBookedEvent event) {
        IssueProformaCommand command = translator.translate(event);
        proformaUseCase.issueProforma(command);
    }
}
