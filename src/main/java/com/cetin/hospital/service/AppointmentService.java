package com.cetin.hospital.service;

import com.cetin.hospital.model.Appointment;
import com.cetin.hospital.model.Doctor;
import com.cetin.hospital.model.Patient;
import com.cetin.hospital.model.Time;
import com.cetin.hospital.repository.AppointmentRepository;
import com.cetin.hospital.request.AppointmentRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final TimeService timeService;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorService doctorService,
                              PatientService patientService, TimeService timeService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.timeService = timeService;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("Invalid appointmentId"));
    }

    public Appointment createAppointment(AppointmentRequest appointmentRequest) {
        Doctor doctor = doctorService.getDoctorById(appointmentRequest.getDoctorId());
        Patient patient = patientService.getPatientById(appointmentRequest.getPatientId());
        Time time = timeService.getTimeById(appointmentRequest.getTimeId());
        if (doctor == null || patient == null || time == null) {
            throw new EntityNotFoundException("Invalid doctorId or patientId or timeId");
        }
        timeService.updateTimeStatus(time.getId(), true);
        Appointment appointment = Appointment.builder().
                doctor(doctor).
                patient(patient).
                time(time).
                build();
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointmentById(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        timeService.updateTimeStatus(appointment.getTime().getId(), false);
        appointmentRepository.deleteById(appointmentId);
    }
}
