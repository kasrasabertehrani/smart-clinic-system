package com.billingcontext.domain.payment;

import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.shared.DomainEvent;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.id.PatientId;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public class Payment {
    private final PaymentId paymentId;
    private final PatientId patientId;
    private final InvoiceId invoiceId;
    private final Money amountOwed;
    private final PaymentType paymentType;
    private PaymentStatus paymentStatus;
    private final TransactionId transactionId;
    private final LocalDateTime createdAt;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Payment(PaymentId paymentId, PatientId patientId, InvoiceId invoiceId, Money amountOwed,
                   PaymentType paymentType, PaymentStatus paymentStatus, TransactionId transactionId, LocalDateTime createdAt) {
        if(amountOwed.isZero()) {
            throw new PaymentException("Amount owed cannot be zero");
        }
        this.paymentId = paymentId;
        this.patientId = patientId;
        this.invoiceId = invoiceId;
        this.amountOwed = amountOwed;
        this.paymentType = paymentType;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.createdAt = createdAt;
    }
    public static Payment paymentDeduction(PatientId patientId, InvoiceId invoiceId, Money amountOwed, TransactionId transactionId) {
        return new Payment(
                new PaymentId(UUID.randomUUID().toString()),
                patientId,
                invoiceId,
                amountOwed,
                PaymentType.PAYMENT_DEDUCTION,
                PaymentStatus.PENDING,
                transactionId,
                LocalDateTime.now()
        );
    }

    public static Payment refundCredit(Payment originalPayment, Money toBeRefunded, TransactionId  transactionId) {

        if (originalPayment.getPaymentType() != PaymentType.PAYMENT_DEDUCTION) {
            throw new PaymentException("You can only issue a refund against a payment deduction.");
        }
        if (originalPayment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            throw new PaymentException("Cannot refund a payment that was not successful.");
        }
        if (transactionId == null) {
            throw new PaymentException("Cannot refund a payment without payment intent id.");
        }
        return new Payment(
                new PaymentId(UUID.randomUUID().toString()),
                originalPayment.getPatientId(),
                originalPayment.getInvoiceId(),
                toBeRefunded,
                PaymentType.REFUND,
                PaymentStatus.PENDING,
                transactionId,
                LocalDateTime.now()
        );
    }

    private void markPaymentAsSuccess() {
        this.paymentStatus = this.paymentStatus.changeStatusToSuccess();
    }
    private void markPaymentAsFailed() {
        this.paymentStatus = this.paymentStatus.changeStatusToFail();
    }
    private void markPaymentAsExpired() {
        this.paymentStatus = this.paymentStatus.changeStatusToExpired();
    }

    public void expirePayment(){
        if(!isPending()) {
            throw new PaymentException("Payment cannot be expired" + this.paymentStatus.toString());
        }
        markPaymentAsExpired();
    }

    public void finalizePaymentStatus(TransactionResult result) {
        if (result.isSuccessful()) {
            markPaymentAsSuccess();
            releaseSuccessEvent(result);
        }
        else {
            markPaymentAsFailed();
        }

    }

    private void releaseSuccessEvent(TransactionResult result) {
        if(result.isPayment()) {
            domainEvents.add(new PaymentWasSuccessful(invoiceId, amountOwed));
        }
        else {
            domainEvents.add(new RefundWasSuccessful(invoiceId, amountOwed));
        }
    }

    public boolean isPending() {
        return paymentStatus == PaymentStatus.PENDING;
    }
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    public enum PaymentType{
        PAYMENT_DEDUCTION,
        REFUND
    }

}
