package com.hospital.hospital.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private int amount;
    private LocalDate dueDate;

    @OneToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    private Boolean status;
}
