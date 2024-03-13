package com.hospital.hospital.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "drug")
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String manufacturer;
    private LocalDate expiryDate;
    private int stockQuantity;
    private int price;

    @ManyToMany(mappedBy = "drugs")
    private List<Prescription> prescriptions;
}
