package com.billingcontext.infrastructure.persistence.dao;

import com.billingcontext.domain.invoice.InvoiceStatus;
import com.billingcontext.infrastructure.dto.response.ExpiredInvoiceReport;
import com.billingcontext.infrastructure.dto.response.InvoiceStatusResponse;
import com.billingcontext.infrastructure.persistence.jpa.InvoiceEntity;
import org.springframework.data.repository.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface InvoiceQueryDao extends Repository<InvoiceEntity, String> {

    @Query("SELECT new com.billingcontext.infrastructure.dto.response.InvoiceStatusResponse(" +
            "i.invoiceId, i.doctorId, i.patientId, i.appointmentId, CAST(i.invoiceStatus AS string)) " +
            "FROM InvoiceEntity i WHERE i.appointmentId = :appointmentId")
    Optional<InvoiceStatusResponse> getInvoiceStatus(@Param("appointmentId") String appointmentId);

    @Query("SELECT new com.billingcontext.infrastructure.dto.response.InvoiceStatusResponse(" +
            "i.invoiceId, i.doctorId, i.patientId, i.appointmentId, CAST(i.invoiceStatus AS string)) " +
            "FROM InvoiceEntity i WHERE i.invoiceId = :invoiceId")
    Optional<InvoiceStatusResponse> getInvoiceStatusById(@Param("invoiceId") String invoiceId);

    @Query("SELECT new com.billingcontext.infrastructure.dto.response.InvoiceStatusResponse(" +
            "i.invoiceId, i.appointmentId, i.doctorId, i.patientId, CAST(i.invoiceStatus AS string)) " +
            "FROM InvoiceEntity i WHERE i.invoiceStatus = :status ORDER BY i.createdAt DESC LIMIT 1")
    Optional<InvoiceStatusResponse> getLatestInvoiceByStatus(@Param("status") InvoiceStatus status);

    @Query("SELECT new com.billingcontext.infrastructure.dto.response.ExpiredInvoiceReport(" +
            "i.invoiceId, i.patientId, i.expirationDate, i.updatedAt) " +
            "FROM InvoiceEntity i " +
            "WHERE i.invoiceStatus = 'EXPIRED' " +
            "AND i.updatedAt >= :startOfDay " +
            "AND i.updatedAt <= :endOfDay")
    List<ExpiredInvoiceReport> findExpiredInvoicesForTimeframe(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

}
