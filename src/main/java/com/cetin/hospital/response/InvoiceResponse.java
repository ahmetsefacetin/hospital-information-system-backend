package com.cetin.hospital.response;

import com.cetin.hospital.model.Invoice;
import com.cetin.hospital.model.Prescription;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceResponse {
    private Long id;
    private Long patientId;
    private Integer amount;
    private LocalDate dueDate;
    private PrescriptionResponse prescription;

    public InvoiceResponse(Invoice invoice) {
        this.id = invoice.getId();
        this.patientId = invoice.getPatient().getId();
        this.amount = invoice.getAmount();
        this.dueDate = invoice.getDueDate();
        this.prescription = new PrescriptionResponse(invoice.getPrescription());
    }
}
