package com.billingcontext.infrastructure.persistence.repository.payment;

import com.billingcontext.infrastructure.persistence.jpa.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

    List<PaymentEntity> findByInvoiceIdIn(List<String> invoiceIds);
    Optional<PaymentEntity> findByTransactionId(String transactionId);

    @Query("SELECT p FROM PaymentEntity p WHERE p.invoiceId = :invoiceId AND p.paymentStatus = 'SUCCESS'")
    Optional<PaymentEntity> findSuccessfulPaymentByInvoiceId(@Param("invoiceId") String invoiceId);

}
