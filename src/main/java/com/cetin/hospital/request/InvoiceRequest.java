package com.cetin.hospital.request;

import lombok.Data;

@Data
public class InvoiceRequest {
    private Long patientId;
    private Long prescriptionId;
}
