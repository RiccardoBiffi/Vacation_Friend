package com.rbiffi.vacationfriend.Utils;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

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
    public static Date timestampToDate(Long value) {
        return value == null ? null : new Date(value);
    }
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
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
