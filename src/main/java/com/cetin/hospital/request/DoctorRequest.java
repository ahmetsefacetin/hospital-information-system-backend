package com.cetin.hospital.request;

import lombok.Data;

@Data
public class DoctorRequest {
    private String TC;
    private String name;
    private String password;
    private String specialty;
}
