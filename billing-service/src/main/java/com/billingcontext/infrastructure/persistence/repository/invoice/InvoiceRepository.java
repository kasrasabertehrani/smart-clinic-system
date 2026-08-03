package com.billingcontext.infrastructure.persistence.repository.invoice;

import com.billingcontext.domain.invoice.InvoiceStatus;
import com.billingcontext.infrastructure.persistence.jpa.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, String> {

    Optional<InvoiceEntity> findByAppointmentId(String appointmentId);
    List<InvoiceEntity> findByInvoiceStatusIn(List<InvoiceStatus> statuses);
}
