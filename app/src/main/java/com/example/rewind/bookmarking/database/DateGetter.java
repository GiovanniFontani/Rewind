package com.example.rewind.bookmarking.database;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class DateGetter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatDate(LocalDateTime myDateObj){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss",Locale.ITALIAN);
        return myFormatObj.format(myDateObj);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime getLocalDateTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        return myDateObj;
    }


    // LocalTime to string (hh:mm:ss:ns)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String LocalTimeToString(LocalTime myDateObj){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ITALY);
        return myFormatObj.format(myDateObj);
    }

    // time string of vlc to LocalTime (hh:mm:ss:ns)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalTime stringToLocalTime(String time){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ITALY);
        LocalTime localTime = LocalTime.parse(time, myFormatObj);
        return localTime;
    }
}
