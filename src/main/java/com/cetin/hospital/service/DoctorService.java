package com.cetin.hospital.service;

import com.cetin.hospital.model.Doctor;

import com.cetin.hospital.repository.DoctorRepository;
import com.cetin.hospital.request.DoctorRequest;
import com.cetin.hospital.request.TimeRequest;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final TimeService timeService;

    public DoctorService(DoctorRepository doctorRepository, TimeService timeService) {
        this.doctorRepository = doctorRepository;
        this.timeService = timeService;
    }

    public Doctor createDoctor(DoctorRequest doctorRequest) {
        Doctor doctor = doctorRepository.findByTC(doctorRequest.getTC());
        if (doctor != null) throw new EntityExistsException("There is already a doctor with this TC.");
        doctor = Doctor.builder().
                TC(doctorRequest.getTC()).
                name(doctorRequest.getName()).
                password(doctorRequest.getPassword()).
                specialty(doctorRequest.getSpecialty()).
                build();

        doctorRepository.save(doctor);

        createDoctorClocks(doctor);

        return doctor;
    }

    public void createDoctorClocks(Doctor doctor) {

        LocalDate currentDate = LocalDate.now();

        // 20 gün sonrasının tarihini hesapla
        LocalDate futureDate = currentDate.plusDays(20);

        // Hafta sonlarını içermeyen günleri hesapla
        List<LocalDate> workDays = calculateWorkDays(currentDate, futureDate);

        // Randevu saatlerini belirle
        List<LocalTime> clocks = generateClocks();

        // Her çalışma günü için randevu oluştur
        for (LocalDate date : workDays) {
            for (LocalTime time : clocks) {
                TimeRequest timeRequest = new TimeRequest();
                timeRequest.setDoctor(doctor);
                LocalDateTime dateTime = LocalDateTime.of(date, time);
                timeRequest.setTime(dateTime);

                timeService.createTime(timeRequest);
            }
        }

    }

    private List<LocalDate> calculateWorkDays(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> workDays = new ArrayList<>();
        LocalDate date = startDate.plusDays(1);
        while (!date.isAfter(endDate)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workDays.add(date);
            }
            date = date.plusDays(1);
        }
        return workDays;
    }

    private List<LocalTime> generateClocks() {
        List<LocalTime> appointmentTimes = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(16, 0);
        while (!startTime.isAfter(endTime)) {
            appointmentTimes.add(startTime);
            startTime = startTime.plusHours(1);
        }
        return appointmentTimes;
    }

}
