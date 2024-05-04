package com.cetin.hospital.service;

import com.cetin.hospital.model.Doctor;
import com.cetin.hospital.model.Time;
import com.cetin.hospital.repository.DoctorRepository;
import com.cetin.hospital.repository.TimeRepository;
import com.cetin.hospital.request.TimeRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeService {
    private final TimeRepository timeRepository;
    private final DoctorRepository doctorRepository;

    public TimeService(TimeRepository timeRepository, DoctorRepository doctorRepository) {
        this.timeRepository = timeRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public List<Time> getTimesByDoctorId(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new EntityNotFoundException("Invalid doctorId"));

        if (isLastClockApproaching(doctorId, 15)) {
            LocalDate currentDate = LocalDate.now();
            LocalDate futureDate = currentDate.plusDays(20);

            LocalDate lastClock = findLastClockByDoctorId(doctorId).toLocalDate();

            createDoctorClocks(doctor, lastClock.plusDays(1), futureDate);
            deleteOldClocks(doctorId, 10);
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(7);
        return timeRepository.findTimesByDoctorId(doctorId, now, endDate);
    }

    public List<Time> getTimesByDoctorTC(String doctorTC) {
        Doctor doctor = doctorRepository.findByTC(doctorTC);
        if (doctor == null) throw new EntityNotFoundException("Invalid doctor TC.");
        return getTimesByDoctorId(doctor.getId());
    }

    public Time getTimeById(Long timeId) {
        return timeRepository.findById(timeId).orElseThrow(() -> new EntityNotFoundException("Invalid timeId"));
    }

    public void createTime(TimeRequest timeRequest) {
        Time clock = Time.builder().
                time(timeRequest.getTime()).
                doctor(timeRequest.getDoctor()).
                status(false).
                build();
        timeRepository.save(clock);
    }

    public void updateTimeStatus(Long timeId, Boolean status) {
        Time time = getTimeById(timeId);
        time.setStatus(status);
        timeRepository.save(time);
    }

    public void createDoctorClocks(Doctor doctor, LocalDate startDate, LocalDate endDate) {

        List<LocalDate> workDays = calculateWorkDays(startDate, endDate);

        List<LocalTime> clocks = generateClocks();

        for (LocalDate date : workDays) {
            for (LocalTime time : clocks) {
                TimeRequest timeRequest = new TimeRequest();
                timeRequest.setDoctor(doctor);
                LocalDateTime dateTime = LocalDateTime.of(date, time);
                timeRequest.setTime(dateTime);

                createTime(timeRequest);
            }
        }

    }

    public List<LocalDate> calculateWorkDays(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> workDays = new ArrayList<>();
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workDays.add(date);
            }
            date = date.plusDays(1);
        }
        return workDays;
    }

    public List<LocalTime> generateClocks() {
        List<LocalTime> clocks = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(16, 0);
        while (!startTime.isAfter(endTime)) {
            clocks.add(startTime);
            startTime = startTime.plusHours(1);
        }
        return clocks;
    }

    public void deleteOldClocks(Long doctorId, Integer cutOffDateLimit) {
        LocalDateTime cutOffDate = LocalDateTime.now().minusDays(cutOffDateLimit);
        timeRepository.deleteOldClocks(cutOffDate, doctorId);
    }

    public LocalDateTime findLastClockByDoctorId(Long doctorId) {
        return timeRepository.findLastClockByDoctorId(doctorId);
    }

    public boolean isLastClockApproaching(Long doctorId, Integer thresholdDays) {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime lastClock = timeRepository.findLastClockByDoctorId(doctorId);
        long daysUntilLastDay = ChronoUnit.DAYS.between(currentDate, lastClock);
        return daysUntilLastDay < thresholdDays;
    }

}
