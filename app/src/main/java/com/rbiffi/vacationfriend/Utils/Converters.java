package com.rbiffi.vacationfriend.Utils;

import android.arch.persistence.room.TypeConverter;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// per convertire tipi di dato da-a.
// Usato anche per tipi che Room può persistere se annotati con @TypeConverter
public class Converters {

    //BOOLEAN
    @TypeConverter
    public static Boolean intToBoolean(int value) {
        return value != 0;
    }
    @TypeConverter
    public static int booleanToInt(Boolean bool) {
        if(bool == null) return 0;
        return bool ? 1 : 0;
    }

    //DATE & TIME
    @TypeConverter
    public static Date timestampToDate(String value) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        try {
            date = value == null ? null : format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Nullable
    public static Date userInterfaceToDate(String value) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        try {
            date = value == null ? null : format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Nullable
    @TypeConverter
    public static String dateToTimestamp(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return date == null ? null : format.format(date);
    }

    @Nullable
    public static String dateToUserInterface(Date date) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return date == null ? null : format.format(date);
    }

    @NonNull
    public static String timeToUserInterface(Time time) {
        DateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return time == null ? "" : format.format(time);
    }

    //IMAGES
    @TypeConverter
    public static Uri stringToUri(String path) {
        return path == null ? null : Uri.parse(path);
    }
    @TypeConverter
    public static String uriToString(Uri path) {
        return path == null ? null : path.toString();
    }

    // DP - PIXELS
    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
