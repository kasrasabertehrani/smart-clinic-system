package com.billingcontext.application.usecase.cancellation;

import com.billingcontext.infrastructure.dto.response.InvoiceResponse;

public interface InitiateCancellationUseCase {
    void initiateCancellation(InvoiceCancellationCommand command);
}
