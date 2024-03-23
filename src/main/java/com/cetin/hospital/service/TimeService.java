package com.cetin.hospital.service;

import com.cetin.hospital.model.Time;
import com.cetin.hospital.repository.TimeRepository;
import com.cetin.hospital.request.TimeRequest;
import org.springframework.stereotype.Service;

@Service
public class TimeService {
    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public void createTime(TimeRequest timeRequest) {
        Time clock = Time.builder()
                .time(timeRequest.getTime())
                .doctor(timeRequest.getDoctor())
                .status(false)
                .build();
        timeRepository.save(clock);
    }
}
