package org.izv.proyecto.view.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Time {
    private static final String FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT, Locale.GERMANY);
        Date date = new Date(System.currentTimeMillis());
        String timeFormatted = formatter.format(date);
        return timeFormatted;
    }

    public static String getTimeFormatted(String format, String time) {
        SimpleDateFormat readingFormat = new SimpleDateFormat(FORMAT, Locale.GERMANY);
        SimpleDateFormat outputFormat = new SimpleDateFormat(format, Locale.GERMANY);
        Date date = null;
        try {
            date = readingFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(date);
    }
}
