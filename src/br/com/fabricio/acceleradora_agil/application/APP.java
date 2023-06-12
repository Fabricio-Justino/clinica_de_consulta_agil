package br.com.fabricio.acceleradora_agil.application;

import br.com.fabricio.acceleradora_agil.model.Appointment;
import br.com.fabricio.acceleradora_agil.model.Patient;
import br.com.fabricio.acceleradora_agil.singleton.AppointmentManager;
import br.com.fabricio.acceleradora_agil.utils.Formatter;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class APP {
    private static String[] CHOISES;
    private static final Scanner NUMBER_SCANNER = new Scanner(System.in);
    private static final Scanner LINE_SCANNER = new Scanner(System.in);




    private static String inputln(String message) {
        System.out.println(message);
        return LINE_SCANNER.nextLine();
    }

    private static String input(String message) {
        System.out.print(message);
        return LINE_SCANNER.nextLine();
    }

    private static boolean exit() {
        System.out.println("=+".repeat(9));
        System.out.println("SALVANDO DADOS ...");
        AppointmentManager.ISTANCE.saveData();
        System.out.println("DADOS SALVOS COM SUCESSO!");
        System.out.println("=+".repeat(9));

        System.out.println("Obrigado por usar nosso sistema");
        return  true;
    }

    private static boolean addNewPatient() {
        String name = input("Qual seu nome: ");
        String number = input("Qual seu Telefone: ");
        Patient patient = new Patient(name, number);
        AppointmentManager.ISTANCE.addPatient(patient);

        System.out.printf("%s cadastrado no sistema com sucesso %n", patient);
        return false;
    }


    private static int[] getCorretData() {
        int year = 0;
        int month = 1;
        int day = 1;
        boolean wrongFormat = true;

        while (wrongFormat) {
            String date = input("Digite uma data para consulta, formato dd/mm/aaaa: ");
            wrongFormat = !date.matches("\\d{2}/\\d{2}/\\d{4}");
            if (wrongFormat) System.out.println("FORMATO DE DATA ERRADO");
            else {
                String[] dateSplitted = date.split("/");
                year = Integer.parseInt(dateSplitted[2]);
                month = Integer.parseInt(dateSplitted[1]);
                day = Integer.parseInt(dateSplitted[0]);

                if (month <= 0 || month > 12) {
                    System.out.println("Mes com formato errado: minimo 1 | maximo 12");
                    wrongFormat = true;
                }

                if (day <= 0 || day > 31) {
                    System.out.println("Dia com formato errado: minimo 1 | maximo 31");
                    wrongFormat = true;
                }
            }
        }

        return new int[]{day, month, year};
    }

    private static int getCorretHours() {
        boolean wrongFormat = true;
        String hour = "";
        int hourInt = 0;

        wrongFormat = true;
        while (wrongFormat) {
            hour = input("Digite uma hora para sua consulta de 0 à 23: ");
            try {
                hourInt = Integer.parseInt(hour);
                wrongFormat = !(hourInt >= 0 && hourInt <= 23);
                if (wrongFormat) System.out.println("DIGITE NO INTERVALO CERTO!!");
            } catch (NumberFormatException ex) {
                System.out.println("Digite um número");
            }
        }

        return  hourInt;
    }

    private static LocalDateTime getLocalDateTime() {
        LocalDateTime localDateTime;
        int[] dateSplited = getCorretData();
        final int YEAR = 2, MONTH = 1, DAY = 0;
        int hour = getCorretHours();

        localDateTime = LocalDateTime.of(dateSplited[YEAR], dateSplited[MONTH], dateSplited[DAY], hour, 0, 0);
        return localDateTime;
    }

    private static boolean addNewAppointment() {
        String number = input("Qual o seu número: ");
        LocalDateTime localDateTime;

        Patient patient = AppointmentManager.ISTANCE.findPatientByPhone(number).orElseThrow(() -> new IllegalArgumentException("Não há paciente com o número: " + number));
        localDateTime = getLocalDateTime();
        System.out.println("+=".repeat(10));

        System.out.println("Digite a especialidade da consulta");

        final String info = LINE_SCANNER.nextLine();

        Appointment ap = new Appointment(patient, localDateTime, info);
        AppointmentManager.ISTANCE.addAppointment(ap);

        System.out.println("Consulta Agendada com sucesso");
        System.out.println("============ DADOS DA CONSULTA ============");
        System.out.println(ap);
        return false;
    }



    private static boolean removeAppointment() {
        LocalDateTime date = getLocalDateTime();
        Appointment appointment = AppointmentManager.ISTANCE.findByDate(date)
                .orElseThrow(() -> new IllegalArgumentException("Não há consulta com a data: "
                        + date.format(Formatter.toDDMMYYYY())));

        AppointmentManager.ISTANCE.removeAppointment(appointment);
        System.out.println("Consulta para a data: " + appointment.getDate().format(Formatter.toDDMMYYYY()) + " Cancelada com sucesso!");
        return false;
    }

    private static void makeChoise() {
        if (CHOISES == null) {
            CHOISES = """
                    Sair,Cadastra novo paciente,Agendar consulta,Cancelar consulta""".split(",");
        }

        for (int i = 0; i < CHOISES.length; ++i) {
            System.out.printf("[%d] - %s%n", i, CHOISES[i]);
        }
    }

    private static int getChoise() {
        int choise = 0;
        boolean repeatConditional;
        do {
            try {
                System.out.print("Escolha: ");
                choise = NUMBER_SCANNER.nextInt();
                repeatConditional = choise < 0 || choise >= CHOISES.length;

                if (repeatConditional) {
                    System.out.println("Escolha uma opção válida");
                }

            } catch (InputMismatchException ex) {
                NUMBER_SCANNER.nextLine();
                System.out.println("DIGITE APENAS Número");
                repeatConditional = true;
            }


        } while (repeatConditional);

        return choise;
    }

    public static void start() {

        boolean exit = false;
        while (!exit) {
            makeChoise();

            try {
                exit = switch (getChoise()) {
                    case 0 -> exit();
                    case 1 -> addNewPatient();
                    case 2 -> addNewAppointment();
                    case 3 -> removeAppointment();
                    default -> throw new IllegalStateException("Unexpected value: " + getChoise());
                };
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                exit = false;
            }
        }

    }


}
