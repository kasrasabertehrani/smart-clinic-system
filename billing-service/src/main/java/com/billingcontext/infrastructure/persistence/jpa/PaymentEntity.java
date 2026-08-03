package com.billingcontext.infrastructure.persistence.jpa;


import com.billingcontext.domain.payment.Payment;
import com.billingcontext.domain.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id
    String paymentId;

    @Column(name = "patient_id", nullable = false)
    String patientId;

    @Column(name = "invoice_id", nullable = false)
    String invoiceId;

    @Column(name = "base_currency", nullable = false)
    String currency;

    @Column(name = "amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amountOwed;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    Payment.PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    PaymentStatus paymentStatus;

    @Column(name = "transaction_id")
    String transactionId;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    public PaymentEntity(String paymentId, String patientId, String invoiceId, String currency,
                         BigDecimal amountOwed, Payment.PaymentType paymentType, PaymentStatus paymentStatus,
                         String transactionId, LocalDateTime createdAt) {

        this.paymentId = paymentId;
        this.patientId = patientId;
        this.invoiceId = invoiceId;
        this.currency = currency;
        this.amountOwed = amountOwed;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.createdAt = createdAt;
    }

    public PaymentEntity() {}

}
