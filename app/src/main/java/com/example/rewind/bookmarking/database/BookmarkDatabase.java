package com.example.rewind.bookmarking.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Bookmark.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class BookmarkDatabase extends RoomDatabase {
    public static final String DB_NAME = "bookmarkDB";
    private static volatile BookmarkDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    @RequiresApi(api = Build.VERSION_CODES.O)
    static  BookmarkDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (BookmarkDatabase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(), BookmarkDatabase.class, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
                SharedPreferences prefs = context.getSharedPreferences("com.example.rewind", Context.MODE_PRIVATE);
                if (prefs.getBoolean("firstrun", true)) {
                    Bookmark example = new Bookmark("example_Name", "example_Content", DateGetter.getLocalDateTime(), "example_video_Name", null, 1, DateGetter.stringToLocalTime("00:00:00"));
                    BookmarkDatabase.databaseWriteExecutor.execute(() -> {
                        instance.bookmarkDao().insertAll(example);
                    });
                    prefs.edit().putBoolean("firstrun", false).apply();
                }
            }
        }
        return instance;
    }

    public abstract BookmarkDAO bookmarkDao();
}
