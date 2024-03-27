package com.cetin.hospital.response;

import com.cetin.hospital.model.Appointment;
import lombok.Data;

@Data
public class AppointmentResponse {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private Long timeId;

    public AppointmentResponse(Appointment appointment) {
        this.id = appointment.getId();
        this.doctorId = appointment.getDoctor().getId();
        this.patientId = appointment.getPatient().getId();
        this.timeId = appointment.getTime().getId();
    }
}
