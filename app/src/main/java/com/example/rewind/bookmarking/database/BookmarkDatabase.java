package com.example.rewind.bookmarking.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
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

    static  BookmarkDatabase getDatabase(final Context context){
        if(instance == null) {
            synchronized (BookmarkDatabase.class) {
                try {
                    if (Arrays.asList(context.getApplicationContext().getResources().getAssets().list("")).contains("bookmarkDB.db")) {
                        Room.databaseBuilder(context.getApplicationContext(), BookmarkDatabase.class, DB_NAME + ".db")
                                .createFromAsset("database/" + DB_NAME + ".db")
                                .build();
                    } else {
                        instance = Room.databaseBuilder(context.getApplicationContext(), BookmarkDatabase.class, DB_NAME)
                                .fallbackToDestructiveMigration()
                                .build();
                        Bookmark example = new Bookmark("example-Name", "example-Content", Calendar.getInstance().getTime());
                        BookmarkDatabase.databaseWriteExecutor.execute(() -> {
                            instance.bookmarkDao().insertAll(example);
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return instance;
    }


    public abstract BookmarkDAO bookmarkDao();
}
