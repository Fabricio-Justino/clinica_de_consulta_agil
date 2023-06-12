package br.com.fabricio.acceleradora_agil.singleton;

import br.com.fabricio.acceleradora_agil.model.Appointment;
import br.com.fabricio.acceleradora_agil.model.Patient;
import br.com.fabricio.acceleradora_agil.utils.Formatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public enum AppointmentManager {
    ISTANCE;

    private HashSet<Appointment> appointments;
    private HashSet<Patient> patients;

    AppointmentManager() {
       this.loadData();
    }

    public void addPatient(Patient patient) {
        if (!this.patients.add(patient))
            throw new IllegalArgumentException("Paciente com o número %s já foi cadastrado!".formatted(patient.getPhone()));
    }

    public void addAppointment(Appointment appointment) {
        LocalDateTime now = LocalDateTime.now();
        if (appointment.getDate().isBefore(now)) throw new IllegalArgumentException("Data da consulta deve ser depois de hoje (%s)"
                .formatted(now.format(Formatter.toDDMMYYYY())));

        if (!this.appointments.add(appointment))
            throw new IllegalArgumentException("Já há uma consulta marcada como a data: %s"
                    .formatted(appointment.getDate().format(Formatter.toDDMMYYYY())) );
    }

    public void addAppointment(Patient patient, LocalDateTime data, String appointmentInfo) {
        Appointment appointment = new Appointment(patient, data, appointmentInfo);
        this.addAppointment(appointment);
    }

    public Optional<Patient> findPatientByPhone(final String phone) {
        return this.patients.parallelStream().filter(patient -> patient.getPhone().equals(phone)).findFirst();
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public void removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
    }

    public Optional<Appointment> findByDate(LocalDateTime date) {
        return this.appointments.parallelStream().filter(appo -> appo.getDate().isEqual(date)).findFirst();
    }

    public void saveData() {
        CRUD_Prototype.ISTANCE.save(this.patients, "patients.ser");
        CRUD_Prototype.ISTANCE.save(this.appointments, "appointments.ser");
        this.logDiagnostics();
        this.logPatients();
    }

    private void logDiagnostics() {
        final StringBuilder LOG = new StringBuilder();
        LOG.append("<div style=\"text-align: center;\"><h1>Consultas marcadas</h1></div>").append("\n\n");

        if (this.appointments.isEmpty()) {
            LOG.append("## Infelismente ainda não há consultas marcadas");
        } else {
            LOG.append("| Paciente | Núemro de telefone | data da consulta | especialidade da consulta |").append('\n');
            LOG.append("| --- | --- | --- | --- |").append('\n');
            this.appointments.stream().sorted(Comparator.comparing(Appointment::getDate)).forEach(appo -> {
                LOG.append("| %s | %s | %s | %s |".formatted(appo.getPatient().getName(),
                                Formatter.phoneFormatter(appo.getPatient().getPhone()),
                                appo.getDate().format(Formatter.toDDMMYYYY()),
                                appo.getAppointmentInfo()))
                        .append('\n');
            });
        }

        createDirectoryIfWasNotCreated(LOG, "consultas.md");
    }

    private void logPatients() {
        final StringBuilder LOG = new StringBuilder();
        LOG.append("<div style=\"text-align: center;\"><h1>Pacientes cadastrados</h1></div>").append("\n\n");

        if (this.appointments.isEmpty()) {
            LOG.append("## Infelismente ainda não há pacientes cadastrados");
        } else {
            LOG.append("| Paciente | Núemro de telefone |").append('\n');
            LOG.append("| --- | --- |").append('\n');
            this.patients.stream().sorted(Comparator.comparing(Patient::getName)).forEach(patient -> {
                LOG.append("| %s | %s |".formatted(patient.getName(),
                         Formatter.phoneFormatter(patient.getPhone())))
                        .append('\n');
            });
        }

        createDirectoryIfWasNotCreated(LOG, "pacientes.md");
    }

    private void createDirectoryIfWasNotCreated(StringBuilder LOG, String filename) {
        File file = new File("./dados_cadastrais");
        if (!file.exists()) {
            if (!file.mkdir()) {
                System.err.println("DIRETORIO NÂO CRIADO");
            }
        }

        try (FileWriter writer = new FileWriter(file.getPath() + "/" + filename)) {
            writer.append(LOG.toString());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    public void loadData() {
        this.patients = CRUD_Prototype.ISTANCE.load("patients.ser");
        this.appointments = CRUD_Prototype.ISTANCE.load("appointments.ser");

        if (this.patients == null) {
            this.patients = new HashSet<>();
        }

        if (this.appointments == null) {
            this.appointments = new HashSet<>();
        }
    }

}
