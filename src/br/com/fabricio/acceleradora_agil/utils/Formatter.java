package br.com.fabricio.acceleradora_agil.utils;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Formatter {
    private static DateTimeFormatter DATA_FORMATTER_DD_MM_YYYY_HH_MM_SS;


    public static String phoneFormatter(String phone) {
        final int PHONE_NUMBER_WITHOUT_DD = 9;
        final int FULL_PHONE_NUMBER = 11;

        String regex = "";
        String replacement = "";
        if (phone.length() == FULL_PHONE_NUMBER) {
            regex = "(\\d{2})?(\\d{4,5})(\\d{4})";
            replacement = "($1) $2-$3";
        } else if (phone.length() == PHONE_NUMBER_WITHOUT_DD) {
            regex = "(\\d{4,5})(\\d{4})";
            replacement = "$1-$2";
        }

        return phone.replaceAll(regex, replacement);
    }

    public static DateTimeFormatter toDDMMYYYY() {
        if (DATA_FORMATTER_DD_MM_YYYY_HH_MM_SS == null) {
            DATA_FORMATTER_DD_MM_YYYY_HH_MM_SS = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
        }

        return DATA_FORMATTER_DD_MM_YYYY_HH_MM_SS;
    }
}
