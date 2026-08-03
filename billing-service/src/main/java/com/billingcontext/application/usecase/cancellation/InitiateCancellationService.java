package com.billingcontext.application.usecase.cancellation;

import com.billingcontext.application.repository.InvoiceRepositoryPort;

import com.billingcontext.domain.invoice.AppointmentInvoice;
import com.billingcontext.domain.policy.refund.RefundPolicyResult;
import com.billingcontext.domain.policy.refund.RefundPolicyService;
import com.billingcontext.domain.policy.refund.evaluation.RefundEvaluationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class InitiateCancellationService implements InitiateCancellationUseCase {
    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final RefundPolicyService refundPolicyService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void initiateCancellation(InvoiceCancellationCommand command) {
        AppointmentInvoice invoice = invoiceRepositoryPort.findByAppointmentId(command.appointmentId());
        RefundEvaluationContext context = new RefundEvaluationContext(
                invoice.getTotalAmount(),
                command.initiator(),
                command.durationSinceBooking(),
                command.minutesUntilAppointment()
        );
        RefundPolicyResult result = refundPolicyService.processRefundPolicies(context);
        invoice.processCancellation(result);
        invoiceRepositoryPort.save(invoice);
        invoice.getDomainEvents().forEach(eventPublisher::publishEvent);
        invoice.clearDomainEvents();
    }
}
