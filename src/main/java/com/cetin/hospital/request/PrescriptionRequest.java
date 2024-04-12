package com.cetin.hospital.request;

import lombok.Data;

import java.util.List;

@Data
public class PrescriptionRequest {
    private Long doctorId;
    private Long patientId;
    private List<String> drugNames;
}
