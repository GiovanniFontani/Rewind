package com.example.rewind.bookmarking.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookmarkRepository {
    private BookmarkDAO bookmarkDAO;
    private LiveData<List<Bookmark>> allBookmarks;

    BookmarkRepository(Application application) {
        BookmarkDatabase db = BookmarkDatabase.getDatabase(application);
        bookmarkDAO = db.bookmarkDao();
        allBookmarks = bookmarkDAO.getAll();
    }

    LiveData<List<Bookmark>> getAllBookmarks() {
        return allBookmarks;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Bookmark bookmark) {
        BookmarkDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDAO.insertAll(bookmark);
        });
    }

    void delete(Bookmark bookmark){
        BookmarkDatabase.databaseWriteExecutor.execute(()->{
            bookmarkDAO.delete(bookmark);
        });
    }

    void delete(int bk_id){
        BookmarkDatabase.databaseWriteExecutor.execute(()->{
            bookmarkDAO.delete(bk_id);
        });
    }
}
