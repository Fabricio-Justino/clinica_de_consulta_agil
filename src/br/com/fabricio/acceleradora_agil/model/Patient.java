package br.com.fabricio.acceleradora_agil.model;

import java.io.Serial;
import java.io.Serializable;

public class Patient implements Serializable {

    @Serial
    private static final long serialVersionUID = 2L;


    private final String name;
    private String phone;

    public Patient(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return """
                Paciente: %s
                Telfone: %s
                """.formatted(this.name, this.phone);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Patient paciente = (Patient) o;

        return getPhone() != null ? getPhone().equals(paciente.getPhone()) : paciente.getPhone() == null;
    }

    @Override
    public int hashCode() {
        return getPhone() != null ? getPhone().hashCode() : 0;
    }
}
