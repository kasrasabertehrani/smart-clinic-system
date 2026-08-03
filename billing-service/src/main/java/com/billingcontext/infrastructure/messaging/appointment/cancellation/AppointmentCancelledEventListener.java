package com.billingcontext.infrastructure.messaging.appointment.cancellation;

import com.billingcontext.application.usecase.cancellation.InitiateCancellationUseCase;
import com.billingcontext.application.usecase.cancellation.InvoiceCancellationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class AppointmentCancelledEventListener {

    private final AppointmentCancelledTranslator translator;
    private final InitiateCancellationUseCase initiateCancellationUseCase;

    @RabbitListener(queues = "${app.messaging.queues.appointment-canceled}")
    public void onAppointmentCancelled(AppointmentCancelledEvent event) {
        InvoiceCancellationCommand command = translator.translate(event);
        initiateCancellationUseCase.initiateCancellation(command);
    }
}
