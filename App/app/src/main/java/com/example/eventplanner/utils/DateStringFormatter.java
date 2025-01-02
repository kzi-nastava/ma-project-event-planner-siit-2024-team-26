package com.example.eventplanner.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateStringFormatter {

    public static String format(String givenDateString, String wantedFormat){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat(wantedFormat);

        String foramttedString = null;
        try {
            Date date = inputFormat.parse(givenDateString);
            foramttedString = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return foramttedString;
    }
    
    public static LocalDateTime convertISOFormatToLocalDateTime(String givenString){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return LocalDateTime.parse(givenString, formatter);
    }

    public static String LocalDateTimeToString(LocalDateTime time, String format){
        if (format.equals("iso")) {
            return time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return time.format(formatter);
        }
    }

}
