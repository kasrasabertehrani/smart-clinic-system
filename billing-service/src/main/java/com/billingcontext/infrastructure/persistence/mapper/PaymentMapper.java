package com.billingcontext.infrastructure.persistence.mapper;

import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.payment.Payment;
import com.billingcontext.domain.payment.PaymentId;
import com.billingcontext.domain.payment.TransactionId;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.id.PatientId;
import com.billingcontext.infrastructure.persistence.jpa.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentEntity toEntity(Payment payment) {
        if (payment == null) {
            return null;
        }
        return new PaymentEntity(
                payment.getPaymentId().value(),
                payment.getPatientId().value(),
                payment.getInvoiceId().value(),
                payment.getAmountOwed().currency().toString(),
                payment.getAmountOwed().amount(),
                payment.getPaymentType(),
                payment.getPaymentStatus(),
                payment.getTransactionId().value(),
                payment.getCreatedAt()
        );
    }

    public Payment toPayment(PaymentEntity paymentEntity) {
        if (paymentEntity == null) {
            return null;
        }
        return new Payment(
                new PaymentId(paymentEntity.getPaymentId()),
                new PatientId(paymentEntity.getPatientId()),
                new InvoiceId(paymentEntity.getInvoiceId()),
                Money.of(paymentEntity.getAmountOwed(), paymentEntity.getCurrency()),
                paymentEntity.getPaymentType(),
                paymentEntity.getPaymentStatus(),
                new TransactionId(paymentEntity.getTransactionId()),
                paymentEntity.getCreatedAt()
        );
    }
}
