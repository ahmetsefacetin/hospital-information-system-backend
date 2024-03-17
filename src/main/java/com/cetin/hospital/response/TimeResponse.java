package com.cetin.hospital.response;

import com.cetin.hospital.model.Time;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeResponse {
    private Long id;
    private LocalDateTime time;
    private Long doctorId;
    private Long appointmentId;
    private Boolean status;

    public TimeResponse(Time time) {
        this.id = time.getId();
        this.time = time.getTime();
        this.doctorId = time.getDoctor().getId();
        this.appointmentId = time.getAppointment().getId();
        this.status = time.getStatus();
    }
}
