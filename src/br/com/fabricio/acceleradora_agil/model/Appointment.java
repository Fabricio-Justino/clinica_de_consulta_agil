package br.com.fabricio.acceleradora_agil.model;

import br.com.fabricio.acceleradora_agil.utils.Formatter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Appointment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    Patient patient;
    private final LocalDateTime date;
    private final String appointmentInfo;

    public Appointment(Patient patient, LocalDateTime localDateTime, String appointmentInfo) {
        this.patient = patient;
        this.date = localDateTime;
        this.appointmentInfo = appointmentInfo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;

        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return date != null ? date.hashCode() : 0;
    }

    public void setAppointmentInfo(String appointmentInfo) {
        appointmentInfo = appointmentInfo;
    }

    @Override
    public String toString() {
        return """
                %s
                data: %s
                especialidade: %s
                """.formatted(this.patient, this.date.format(Formatter.toDDMMYYYY()), this.appointmentInfo);
    }

    public Patient getPatient() {
        return patient;
    }

    public String getAppointmentInfo() {
        return appointmentInfo;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
