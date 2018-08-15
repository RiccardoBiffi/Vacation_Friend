package com.rbiffi.vacationfriend.Utils;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

import java.util.Date;

// per convertire tipi di dato da-a tipi che Room può persistere
public class Converters {

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
