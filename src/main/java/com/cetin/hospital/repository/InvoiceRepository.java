package com.cetin.hospital.repository;

import com.cetin.hospital.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findByPrescriptionId(Long prescriptionId);
}
