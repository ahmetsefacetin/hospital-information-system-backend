package com.cetin.hospital.response;

import com.cetin.hospital.model.Drug;
import lombok.Data;

@Data
public class DrugResponse {
    private Long id;
    private String name;
    private Integer price;
    private Long prescriptionId;

    public DrugResponse(Drug drug) {
        this.id = drug.getId();
        this.name = drug.getName();
        this.price = drug.getPrice();
        this.prescriptionId = drug.getPrescription().getId();
    }
}
