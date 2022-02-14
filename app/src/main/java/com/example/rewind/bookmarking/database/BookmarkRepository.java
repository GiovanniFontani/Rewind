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

    public LiveData<List<Bookmark>> getAllBookmarks() {
        return allBookmarks;
    }

    public LiveData<List<Bookmark>> getBookmarksByDocumentName(String documentName) {
        return bookmarkDAO.findByLinkedDocumentName(documentName);
    }

    public LiveData<List<Bookmark>> getBookmarkByVideoName(String videoName){
        return bookmarkDAO.findByVideoName(videoName);
    }

    public void insert(Bookmark bookmark) {
        BookmarkDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDAO.insertAll(bookmark);
        });
    }

    public void delete(Bookmark bookmark){
        BookmarkDatabase.databaseWriteExecutor.execute(()->{
            bookmarkDAO.delete(bookmark);
        });
    }

    public void delete(int bk_id){
        BookmarkDatabase.databaseWriteExecutor.execute(()->{
            bookmarkDAO.delete(bk_id);
        });
    }

    public void update(int bk_id, String documentName, Uri documentPath, int pageNumber){
        BookmarkDatabase.databaseWriteExecutor.execute(() ->{
            bookmarkDAO.update(bk_id, documentName, documentPath, pageNumber);
        });
    }

    public LiveData<List<Bookmark>> orderByName(boolean ascending, String bookmarkName){
        return bookmarkName.equals("")? bookmarkDAO.getAllOrderedByName(ascending) :
                bookmarkDAO.getAllOrderedByName(ascending,"%"+bookmarkName+"%");
    }

    public LiveData<List<Bookmark>> orderByDate(boolean ascending, String bookmarkName){
        return bookmarkName.equals("")? bookmarkDAO.getAllOrderedByDate(ascending) :
                bookmarkDAO.getAllOrderedByDate(ascending,"%"+bookmarkName+"%");
    }

    public LiveData<List<Bookmark>> orderByVideoName(boolean ascending, String bookmarkName){
        return bookmarkName.equals("")? bookmarkDAO.getAllOrderedByVideoName(ascending):
                bookmarkDAO.getAllOrderedByVideoName(ascending,"%"+bookmarkName+"%");
    }
}
