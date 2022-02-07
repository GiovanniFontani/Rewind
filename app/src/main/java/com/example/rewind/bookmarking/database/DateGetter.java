package com.example.rewind.bookmarking.database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateGetter {
    public static String getDate(){
        DateFormat df = new SimpleDateFormat("dd/MM/yy", Locale.US);
        return df.format(new Date());
    }
}
