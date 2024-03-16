package com.cetin.hospital.request;

import lombok.Data;

@Data
public class PatientRequest {
    private String TC;
    private String name;
    private String password;
}
