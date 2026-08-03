package com.billingcontext.infrastructure.controller.command.payment;




import com.billingcontext.application.usecase.payment.initiate.checkout.InitiateCheckoutUseCase;
import com.billingcontext.application.usecase.proforma.ProformaUseCase;
import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.infrastructure.dto.response.InvoiceResponse;
import com.billingcontext.infrastructure.dto.response.PaymentInitializationResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final ProformaUseCase proformaUseCase;
    private final InitiateCheckoutUseCase initiateCheckoutUseCase;

    @PostMapping("/{invoiceId}/update/proforma")
    public ResponseEntity<InvoiceResponse> updateProforma(@PathVariable String invoiceId) {
        InvoiceResponse response = proformaUseCase.updateProforma(new InvoiceId(invoiceId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{invoiceId}/initiate/payment")
    public ResponseEntity<PaymentInitializationResponse> initiateCheckout(@PathVariable String invoiceId) {
        PaymentInitializationResponse response = initiateCheckoutUseCase.initializePayment(new InvoiceId(invoiceId));
        return ResponseEntity.ok(response);
    }

}