package com.example.rewind.bookmarking.database;

import android.net.Uri;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static Uri fromString(String string) {
        return string.equals("")? null : Uri.parse(string);
    }

    @TypeConverter
    public static String UriToString(Uri uri) {

        return uri == null? "" : uri.toString();
    }
}