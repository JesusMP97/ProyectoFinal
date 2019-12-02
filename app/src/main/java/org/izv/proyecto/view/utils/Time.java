package org.izv.proyecto.view.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Time {
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.GERMANY);
        Date date = new Date(System.currentTimeMillis());
        String timeFormatted = formatter.format(date);
        return timeFormatted;
    }
}
