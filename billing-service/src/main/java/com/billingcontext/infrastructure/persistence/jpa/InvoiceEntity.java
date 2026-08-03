package com.billingcontext.infrastructure.persistence.jpa;


import com.billingcontext.domain.invoice.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Getter;



import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "invoice")
public class InvoiceEntity {

    @Id
    private String invoiceId;

    @Column(name = "appointment_id", nullable = false)
    private String appointmentId;

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "payment_duration_window")
    private LocalDateTime paymentDurationWindow;

    @Column(name = "base_currency", nullable = false)
    private String currency;

    @Column(name = "base_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal basePrice;

    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "total_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalPrice;

    @Column(name = "refund_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal toBeRefunded;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvoiceStatus invoiceStatus;

    public InvoiceEntity(String invoiceId, String appointmentId, String doctorId, String patientId, LocalDateTime expirationDate,
                         LocalDateTime paymentDurationWindow, String currency, BigDecimal basePrice, BigDecimal discountPercentage,
                         BigDecimal totalPrice,  BigDecimal toBeRefunded, LocalDateTime createdAt, LocalDateTime updatedAt,  InvoiceStatus invoiceStatus ) {
        this.invoiceId = invoiceId;
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.expirationDate = expirationDate;
        this.paymentDurationWindow = paymentDurationWindow;
        this.currency = currency;
        this.basePrice = basePrice;
        this.discountPercentage = discountPercentage;
        this.totalPrice = totalPrice;
        this.toBeRefunded = toBeRefunded;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.invoiceStatus = invoiceStatus;

    }


    protected InvoiceEntity() {

    }
}
