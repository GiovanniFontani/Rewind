package com.example.rewind.bookmarking.database;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookmarkViewModel extends AndroidViewModel {

    private final BookmarkRepository repo;

    private final LiveData<List<Bookmark>> allBookmarks;

    public BookmarkViewModel (Application application) {
        super(application);
        repo = new BookmarkRepository(application);
        allBookmarks = repo.getAllBookmarks();
    }

    public LiveData<List<Bookmark>> getAll() { return allBookmarks; }
    public LiveData<List<Bookmark>> getByDocumentName(String documentName) { return repo.getBookmarksByDocumentName(documentName);}
    public LiveData<List<Bookmark>> getByVideoName(String videoName) {return repo.getBookmarkByVideoName(videoName);}

    public void insert(Bookmark bookmark) { repo.insert(bookmark); }
    public void delete(Bookmark bookmark) { repo.delete(bookmark); }
    public void delete(int bk_id) {repo.delete(bk_id); }
    public void update(int bk_id, String documentName, Uri documentPath, int pageNumber) { repo.update(bk_id, documentName, documentPath, pageNumber);}

    //TODO add methods for ordering rows.
}