package com.cetin.hospital.response;

import com.cetin.hospital.model.Invoice;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceResponse {
    private Long patientId;
    private Integer amount;
    private LocalDate dueDate;
    private Long prescriptionId;

    public InvoiceResponse(Invoice invoice) {
        this.patientId = invoice.getPatient().getId();
        this.amount = invoice.getAmount();
        this.dueDate = invoice.getDueDate();
        this.prescriptionId = invoice.getPrescription().getId();
    }
}
