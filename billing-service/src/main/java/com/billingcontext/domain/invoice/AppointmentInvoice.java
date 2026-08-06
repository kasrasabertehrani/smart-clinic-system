package com.billingcontext.domain.invoice;


import com.billingcontext.domain.policy.pricing.PricingPolicyResult;
import com.billingcontext.domain.policy.refund.RefundPolicyResult;
import com.billingcontext.domain.shared.DomainEvent;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.finance.Percentage;
import com.billingcontext.domain.shared.id.DoctorId;
import com.billingcontext.domain.shared.id.PatientId;
import lombok.Getter;


import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Getter
public class AppointmentInvoice {
    private final InvoiceId invoiceId;
    private final AppointmentId appointmentId;
    private final DoctorId doctorId;
    private final PatientId patientId;
    private final LocalDateTime expirationDate;
    private LocalDateTime paymentDurationWindow;
    private final Money basePrice;
    private Percentage discountPercentage;
    private Money totalAmount;
    private Money toBeRefunded;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private InvoiceStatus invoiceStatus;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public AppointmentInvoice(InvoiceId invoiceId, AppointmentId appointmentId, DoctorId doctorId, PatientId patientId,
                              LocalDateTime expirationDate, LocalDateTime paymentDurationWindow, Money basePrice, Percentage discountPercentage,
                              Money totalAmount, Money toBeRefunded, LocalDateTime createdAt, LocalDateTime updatedAt,
                              InvoiceStatus invoiceStatus) {

        if(basePrice.isZero()){
            throw new InvoiceException("Base price cannot be zero");
        }
        this.invoiceId = invoiceId;
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.expirationDate = expirationDate;
        this.paymentDurationWindow = paymentDurationWindow;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
        this.totalAmount = totalAmount;
        this.toBeRefunded = toBeRefunded;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.invoiceStatus = invoiceStatus;
    }

    public static AppointmentInvoice issueProforma(AppointmentId appointmentId,DoctorId doctorId, PatientId patientId,
                                                   LocalDateTime expirationDate, Money basePrice, Clock clock) {
        LocalDateTime now = LocalDateTime.now(clock);

        if(expirationDate.isBefore(now)){
            throw new InvoiceException("Payment due date cannot be in the past");
        }
        return new AppointmentInvoice(
                new InvoiceId(UUID.randomUUID().toString()),
                appointmentId,
                doctorId,
                patientId,
                expirationDate,
                null,
                basePrice,
                Percentage.zero(),
                basePrice,
                Money.zero(basePrice.currency()),
                now,
                now,
                InvoiceStatus.DRAFT
        );

    }
    public void updateProforma(PricingPolicyResult result){
        if(isPaymentPending()){
            return;
        }
        if (!isProforma()){
            throw new InvoiceException("Only proforma invoices can be updated");
        }
        this.discountPercentage = result.discountPercentage();
        this.totalAmount = result.finalPrice();
        this.updatedAt = LocalDateTime.now();
    }

    public void processCheckOut(Duration gracePeriod,Clock clock){
        if(isPaymentPending()){
            return;
        }
        if(!isProforma()){
            throw new InvoiceException("Only proforma invoices can be checked out");
        }
        this.paymentDurationWindow = calculateWindowExpiration(gracePeriod, clock);
        markAsPaymentPending();
    }


    public void processCancellation(RefundPolicyResult result) {
        if(isExpired() || isRefundPending()) {
            return;
        }
        if (isProforma()) {
            markAsCancelled();
            this.paymentDurationWindow = null;
            return;
        }
        if (!isPaid()) {
            throw new InvoiceException("Cannot process cancellation for invoice in state: " + this.invoiceStatus);
        }
        if (!result.isEligible()) {
            markAsCancelled();
            this.paymentDurationWindow = null;
            return;
        }
        if (result.refundAmount().isGreaterThan(this.totalAmount)) {
            throw new InvoiceException("Refund amount cannot exceed the total amount paid.");
        }
        markAsRefundPending();
        this.toBeRefunded = result.refundAmount();
        domainEvents.add(new RefundWasRequested(invoiceId, patientId, toBeRefunded));
    }

    public void confirmTransaction(Money amount) {
        if (this.invoiceStatus == InvoiceStatus.PAYMENT_PENDING) {
            if (!amount.isEqualTo(this.totalAmount)) {
                throw new InvoiceException("Payment amount must exactly match the locked invoice amount.");
            }
            markAsPaid();
            this.paymentDurationWindow = null;

        } else if (this.invoiceStatus == InvoiceStatus.REFUND_PENDING) {
            if (!amount.isEqualTo(this.toBeRefunded)) {
                throw new InvoiceException("Refund amount cannot be greater than the approved refund amount.");
            }
            markAsRefunded();

        } else {
            throw new InvoiceException("Invoice is not awaiting any transaction confirmation. Current status: " + this.invoiceStatus);
        }
    }

    public Optional<AppointmentInvoice> evaluateTimeouts(Clock clock) {

        if (isExpirationDatePassed(clock) && (isProforma() || isPaymentPending())) {
            this.paymentDurationWindow = null;
            markAsExpired();
            domainEvents.add(new InvoiceWasExpired(appointmentId.value()));
            return Optional.of(this);
        }

        if (isPaymentPending() && isPaymentDurationWindowPassed(clock)) {
            this.paymentDurationWindow = null;
            this.discountPercentage = Percentage.zero();
            this.totalAmount = this.basePrice;
            markAsDraft();
            return Optional.of(this);
        }
        return Optional.empty();
    }

    private void markAsDraft(){
        this.invoiceStatus = invoiceStatus.changeStatusToDraft();
        this.updatedAt = LocalDateTime.now();
    }
    private void markAsPaymentPending(){
        this.invoiceStatus = invoiceStatus.changeStatusToPaymentPending();
        this.updatedAt = LocalDateTime.now();
    }
    private void markAsPaid() {
        this.invoiceStatus = invoiceStatus.changeStatusToPaid();
        this.updatedAt = LocalDateTime.now();
    }
    private void markAsRefundPending() {
        this.invoiceStatus = invoiceStatus.changeStatusToRefundPending();
        this.updatedAt = LocalDateTime.now();
    }
    private void markAsCancelled() {
        this.invoiceStatus = invoiceStatus.changeStatusToCancelled();
        this.updatedAt = LocalDateTime.now();
    }
    private void markAsRefunded() {
        this.invoiceStatus = invoiceStatus.changeStatusToRefunded();
        this.updatedAt = LocalDateTime.now();
    }
    private void markAsExpired() {
        this.invoiceStatus = invoiceStatus.changeStatusToExpired();
        this.updatedAt = LocalDateTime.now();
    }

    private LocalDateTime calculateWindowExpiration(Duration gracePeriod, Clock clock) {
        LocalDateTime calculatedWindow = LocalDateTime.now(clock).plus(gracePeriod);
        return calculatedWindow.isBefore(this.expirationDate)
                ? calculatedWindow
                : this.expirationDate;
    }
    public boolean isExpirationDatePassed(Clock clock){
        return this.expirationDate.isBefore(LocalDateTime.now(clock));
    }
    public boolean isPaymentDurationWindowPassed(Clock clock){
        return this.paymentDurationWindow.isBefore(LocalDateTime.now(clock));
    }
    public boolean isExpired(){
        return this.invoiceStatus.equals(InvoiceStatus.EXPIRED);
    }
    public boolean isProforma(){
        return this.invoiceStatus.equals(InvoiceStatus.DRAFT);
    }
    public boolean isRefundPending(){
        return this.invoiceStatus.equals(InvoiceStatus.REFUND_PENDING);
    }
    public boolean isPaymentPending(){
        return this.invoiceStatus.equals(InvoiceStatus.PAYMENT_PENDING);
    }
    public boolean isPaid(){
        return this.invoiceStatus.equals(InvoiceStatus.PAID);
    }
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
