package com.rbiffi.vacationfriend.Utils;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// per convertire tipi di dato da-a tipi che Room può persistere
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

    //DATE
    @TypeConverter
    public static Date timestampToDate(String value) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
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
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return date == null ? null : format.format(date);
    }

    @Nullable
    public static String dateToUserInterface(Date date) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return date == null ? null : format.format(date);
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
}
