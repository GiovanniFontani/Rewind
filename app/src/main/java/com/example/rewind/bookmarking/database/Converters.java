package com.example.rewind.bookmarking.database;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Converters {
    @TypeConverter
    public static Uri fromString(String string) {
        return string.equals("")? null : Uri.parse(string);
    }

    @TypeConverter
    public static String UriToString(Uri uri) {

        return uri == null? "" : uri.toString();
    }

    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String LocalDataTimeToString(LocalDateTime myDateObj){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss", Locale.ITALY);
        return myFormatObj.format(myDateObj);
    }

    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime StringToLocalDateTime(String dataString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd-MM-yyyy HH:mm:ss", Locale.ITALY );
        LocalDateTime dateTime = LocalDateTime.parse(dataString, formatter);
        return dateTime;
    }

    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String LocalTimeToString(LocalTime myDateObj){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ITALY);
        return myFormatObj.format(myDateObj);
    }

    @TypeConverter
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalTime formatLocalTime(String time){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss",Locale.ITALIAN);
        LocalTime localTime = LocalTime.parse(time, myFormatObj);
        return localTime;
    }
}