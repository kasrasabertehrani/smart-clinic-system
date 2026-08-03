package com.billingcontext.application.usecase.proforma;

import com.billingcontext.application.repository.InvoiceRepositoryPort;
import com.billingcontext.application.repository.PricingProfileRepositoryPort;
import com.billingcontext.domain.invoice.AppointmentInvoice;
import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.policy.pricing.PricingPolicyResult;
import com.billingcontext.domain.policy.pricing.PricingPolicyService;
import com.billingcontext.domain.policy.pricing.evaluation.PricingEvaluationContext;
import com.billingcontext.domain.profile.PricingProfile;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.infrastructure.dto.response.InvoiceResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@RequiredArgsConstructor
@Service
@Transactional
public class ProformaService implements ProformaUseCase {
    private final PricingProfileRepositoryPort pricingProfileRepositoryPort;
    private final InvoiceRepositoryPort invoiceRepositoryPort;
    private final PricingPolicyService pricingPolicyService;
    private final Clock clock;


    @Override
    public void issueProforma(IssueProformaCommand issueProformaCommand) {
        PricingProfile pricingProfile = pricingProfileRepositoryPort.findByDoctorId(issueProformaCommand.doctorId());
        Money basePrice = pricingProfile.calculateAppointmentFee(issueProformaCommand.appointmentDuration());
        AppointmentInvoice proforma = AppointmentInvoice.issueProforma(
                issueProformaCommand.appointmentId(),
                issueProformaCommand.doctorId(),
                issueProformaCommand.patientId(),
                issueProformaCommand.expirationDate(),
                basePrice,
                clock
        );
        invoiceRepositoryPort.save(proforma);
    }

    @Override
    public InvoiceResponse updateProforma(InvoiceId invoiceId){
        AppointmentInvoice proforma = invoiceRepositoryPort.findInvoiceById(invoiceId);
        PricingEvaluationContext context = new PricingEvaluationContext(proforma.getBasePrice(),
                proforma.getCreatedAt(),
                proforma.getExpirationDate(),
                clock);
        PricingPolicyResult result = pricingPolicyService.calculateFinalPrice(context);
        proforma.updateProforma(result);
        invoiceRepositoryPort.save(proforma);
        return InvoiceResponse.from(proforma);
    }
}
