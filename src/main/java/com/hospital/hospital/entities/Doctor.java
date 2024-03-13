package com.hospital.hospital.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String TC;
    private String name;
    private String password;

    @OneToMany(mappedBy = "doctor")
    private List<Time> clocks;
}
