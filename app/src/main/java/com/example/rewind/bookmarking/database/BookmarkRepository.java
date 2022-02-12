package com.example.rewind.bookmarking.database;

import android.app.Application;
import android.net.Uri;

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

    LiveData<List<Bookmark>> getBookmarksByDocumentName(String documentName) {
        return bookmarkDAO.findByLinkedDocumentName(documentName);
    }

    LiveData<List<Bookmark>> getBookmarkByVideoName(String videoName){
        return bookmarkDAO.findByVideoName(videoName);
    }

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

    void update(int bk_id, String documentName, Uri documentPath, int pageNumber){
        BookmarkDatabase.databaseWriteExecutor.execute(() ->{
            bookmarkDAO.update(bk_id, documentName, documentPath, pageNumber);
        });
    }

    //TODO add methods for ordering
}
