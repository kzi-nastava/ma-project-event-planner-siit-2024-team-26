package com.example.eventplanner.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
