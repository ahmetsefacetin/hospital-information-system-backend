package com.cetin.hospital.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InvoiceRequest {
    private Long patientId;
    private Long prescriptionId;
}
