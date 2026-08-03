package com.billingcontext.application.usecase.proforma;

import com.billingcontext.domain.invoice.InvoiceId;
import com.billingcontext.infrastructure.dto.response.InvoiceResponse;

public interface ProformaUseCase {
    void issueProforma(IssueProformaCommand issueProformaCommand);
    InvoiceResponse updateProforma(InvoiceId invoiceId);
}
