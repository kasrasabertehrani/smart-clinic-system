package com.billingcontext.infrastructure.controller.query;

import com.billingcontext.domain.invoice.InvoiceStatus;
import com.billingcontext.infrastructure.dto.response.ExpiredInvoiceReport;
import com.billingcontext.infrastructure.dto.response.InvoiceStatusResponse;
import com.billingcontext.infrastructure.persistence.dao.InvoiceQueryDao;
import com.billingcontext.infrastructure.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queries/invoices")
@RequiredArgsConstructor
public class InvoiceQuery {
    private final InvoiceQueryDao invoiceQueryDao;
    private final TimeProvider dateTimeProvider;

    @GetMapping("/appointment/{appointmentId}/status")
    public ResponseEntity<InvoiceStatusResponse> getInvoiceStatus(@PathVariable String appointmentId) {

        InvoiceStatusResponse response = invoiceQueryDao.getInvoiceStatus(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{invoiceId}/status")
    public ResponseEntity<InvoiceStatusResponse> getInvoiceStatusById(@PathVariable String invoiceId) {

        InvoiceStatusResponse response = invoiceQueryDao.getInvoiceStatusById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/latest")
    public ResponseEntity<InvoiceStatusResponse> getLatestInvoice(
            @RequestParam(defaultValue = "DRAFT") InvoiceStatus status) {

        return invoiceQueryDao.getLatestInvoiceByStatus(status)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/expired-invoices/today")
    public ResponseEntity<List<ExpiredInvoiceReport>> getTodayExpiredInvoices() {

        List<ExpiredInvoiceReport> reportData = invoiceQueryDao.findExpiredInvoicesForTimeframe(
                dateTimeProvider.getStartOfToday(),
                dateTimeProvider.getEndOfToday()
        );
        return ResponseEntity.ok(reportData);
    }
}
