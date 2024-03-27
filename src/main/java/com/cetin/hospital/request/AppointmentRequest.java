package com.cetin.hospital.request;

import lombok.Data;

@Data
public class AppointmentRequest {
    private Long patientId;
    private Long doctorId;
    private Long timeId;
}
