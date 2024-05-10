package com.cetin.hospital.response;

import com.cetin.hospital.model.Doctor;
import com.cetin.hospital.model.Time;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeResponse {
    private Long id;
    private LocalDateTime time;
    private Doctor doctor;
    private Boolean status;

    public TimeResponse(Time time) {
        this.id = time.getId();
        this.time = time.getTime();
        this.doctor = time.getDoctor();
        this.status = time.getStatus();
    }
}
