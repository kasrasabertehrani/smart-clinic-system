package com.billingcontext.domain.payment;


import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.domain.shared.finance.Money;
import com.billingcontext.domain.shared.id.PatientId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collection;


import static com.billingcontext.domain.TestFixtures.moneyOf;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    @Test
    void testPaymentDeductionCreation() {
        PatientId patientId = new PatientId("patient-123");
        InvoiceId invoiceId = new InvoiceId("invoice-456");
        Money amountOwed = moneyOf("100.00", "USD");





    }
    @Test
    void testRefundCreditCreation() {




    }
    @Test
    void testPaymentCreationWithZeroAmount() {
        PatientId patientId = new PatientId("patient-123");
        InvoiceId invoiceId = new InvoiceId("invoice-456");
        Money amountOwed = moneyOf("0.0", "USD");



    }
    @Test
    void testCreateARefundWithAnotherRefund() {
        PatientId patientId = new PatientId("patient-789");
        InvoiceId invoiceId = new InvoiceId("invoice-012");
        Money amountOwed = moneyOf("50.00", "USD");





    }



}
