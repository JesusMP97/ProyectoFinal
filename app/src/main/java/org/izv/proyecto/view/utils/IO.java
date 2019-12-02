package org.izv.proyecto.view.utils;

import android.app.Activity;
import android.content.Context;

public class IO {
    public static String readPreferences(Context context, final String fileName, final String KEY, String defaultValue) {
        String value = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
                .getString(KEY, defaultValue);
        return value;
    }

    public static void savePreferences(Context context, final String fileName, final String KEY, String value) {
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
                .edit().putString(KEY, value)
                .apply();
    }
}
