package com.billingcontext.infrastructure.persistence.dao;


import com.billingcontext.domain.invoice.InvoiceStatus;
import com.billingcontext.infrastructure.dto.response.InvoiceStatusResponse;
import com.billingcontext.infrastructure.dto.response.PaymentStatusResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import com.billingcontext.infrastructure.persistence.jpa.PaymentEntity;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface PaymentQueryDao extends Repository<PaymentEntity, String> {


    @Query("SELECT i.paymentId AS paymentId, " +
            "i.paymentType AS paymentType, " +
            "i.paymentStatus AS paymentStatus, " +
            "i.amountOwed AS amount, " +
            "i.currency AS currency " +
            "FROM PaymentEntity i WHERE i.paymentId = :paymentId")
    Optional<PaymentStatusResponse> getPaymentStatus(@Param("paymentId") String paymentId);

}