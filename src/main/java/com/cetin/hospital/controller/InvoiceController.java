package com.cetin.hospital.controller;

import com.cetin.hospital.model.Invoice;
import com.cetin.hospital.response.InvoiceResponse;
import com.cetin.hospital.service.InvoiceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public List<InvoiceResponse> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return invoices.stream().map(InvoiceResponse::new).toList();
    }

    @GetMapping("/{invoiceId}")
    public InvoiceResponse getInvoiceById(@PathVariable Long invoiceId) {
        return new InvoiceResponse(invoiceService.getInvoiceById(invoiceId));
    }

}
