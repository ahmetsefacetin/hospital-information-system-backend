package com.cetin.hospital.response;

import com.cetin.hospital.model.Appointment;
import com.cetin.hospital.model.Patient;
import com.cetin.hospital.model.Time;
import lombok.Data;

@Data
public class AppointmentResponse {
    private Long id;
    private Long doctorId;
    private Patient patient;
    private Time time;

    public AppointmentResponse(Appointment appointment) {
        this.id = appointment.getId();
        this.doctorId = appointment.getDoctor().getId();
        this.patient = appointment.getPatient();
        this.time = appointment.getTime();
    }
}
