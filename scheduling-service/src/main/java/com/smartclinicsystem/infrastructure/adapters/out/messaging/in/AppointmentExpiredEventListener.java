package com.smartclinicsystem.infrastructure.adapters.out.messaging.in;

import com.smartclinicsystem.application.command.CancelAppointmentCommand;
import com.smartclinicsystem.application.port.in.SchedulingUseCase;
import com.smartclinicsystem.domain.Appointment;
import com.smartclinicsystem.domain.vo.AppointmentId;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppointmentExpiredEventListener {

    private final SchedulingUseCase schedulingUseCase;

    @RabbitListener(queues = "${app.messaging.queues.appointment-expired}")
    public void onAppointmentExpired(AppointmentExpiredEvent event) {
        CancelAppointmentCommand command = new CancelAppointmentCommand(
                new AppointmentId(event.appointmentId()),
                Appointment.CancellationInitiator.SYSTEM_AUTOMATION);

        schedulingUseCase.cancelAppointment(command);
    }
}
