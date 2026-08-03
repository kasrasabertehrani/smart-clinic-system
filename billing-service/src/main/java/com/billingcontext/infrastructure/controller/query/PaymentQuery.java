package com.billingcontext.infrastructure.controller.query;


import com.billingcontext.infrastructure.dto.response.PaymentStatusResponse;
import com.billingcontext.infrastructure.persistence.dao.PaymentQueryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/queries/payments")
@RequiredArgsConstructor
public class PaymentQuery {
    private final PaymentQueryDao paymentQueryDao;

    @GetMapping("/{paymentId}/status")
    public ResponseEntity<PaymentStatusResponse> getPaymentStatus(@PathVariable String paymentId) {

        PaymentStatusResponse response = paymentQueryDao.getPaymentStatus(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        return ResponseEntity.ok(response);
    }

}
