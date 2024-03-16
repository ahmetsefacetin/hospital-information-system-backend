package com.cetin.hospital.response;

import com.cetin.hospital.model.Patient;
import lombok.Data;

@Data
public class PatientResponse {
    private Long id;
    private String TC;
    private String name;
    private String password;

    public PatientResponse(Patient patient) {
        this.id = patient.getId();
        this.TC = patient.getTC();
        this.name = patient.getName();
        this.password = patient.getPassword();
    }
}
