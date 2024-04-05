package com.cetin.hospital.response;

import com.cetin.hospital.model.Doctor;
import com.cetin.hospital.model.Patient;
import com.cetin.hospital.model.Prescription;
import lombok.Data;

import java.util.List;

@Data
public class PrescriptionResponse {
    private Long id;
    private Patient patient;
    private Doctor doctor;
    private List<DrugResponse> drugs;

    public PrescriptionResponse(Prescription prescription) {
        this.id = prescription.getId();
        this.patient = prescription.getPatient();
        this.doctor = prescription.getDoctor();
        this.drugs = prescription.getDrugs().stream().map(DrugResponse::new).toList();
    }
}
