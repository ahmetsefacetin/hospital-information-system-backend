package com.cetin.hospital.service;

import com.cetin.hospital.model.Drug;
import com.cetin.hospital.model.Invoice;
import com.cetin.hospital.model.Patient;
import com.cetin.hospital.model.Prescription;
import com.cetin.hospital.repository.InvoiceRepository;
import com.cetin.hospital.repository.PrescriptionRepository;
import com.cetin.hospital.request.InvoiceRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final PatientService patientService;
    private final PrescriptionRepository prescriptionRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, PatientService patientService, PrescriptionRepository prescriptionRepository) {
        this.invoiceRepository = invoiceRepository;
        this.patientService = patientService;
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId).orElseThrow(() -> new EntityNotFoundException("Invalid invoiceId"));
    }

    public Invoice createInvoice(InvoiceRequest invoiceRequest) {
        Patient patient = patientService.getPatientById(invoiceRequest.getPatientId());
        Prescription prescription = prescriptionRepository.findById(invoiceRequest.getPrescriptionId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid prescriptionId"));
        if (patient == null) {
            throw new EntityNotFoundException("Invalid patientId");
        }
        Integer amount = 0;
        for (Drug drug : prescription.getDrugs()) {
            amount += drug.getPrice();
        }
        Invoice invoice = Invoice.builder().
                patient(patient).
                dueDate(LocalDate.now()).
                amount(amount).
                prescription(prescription).
                build();
        return invoiceRepository.save(invoice);
    }
}
