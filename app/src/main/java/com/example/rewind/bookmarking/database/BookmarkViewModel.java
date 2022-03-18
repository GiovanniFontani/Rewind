package com.example.rewind.bookmarking.database;

import android.app.Application;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookmarkViewModel extends AndroidViewModel {

    private final BookmarkRepository repo;

    private final LiveData<List<Bookmark>> allBookmarks;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public BookmarkViewModel (Application application) {
        super(application);
        repo = new BookmarkRepository(application);
        allBookmarks = repo.getAllBookmarks();
    }

    public LiveData<List<Bookmark>> getAll() { return allBookmarks; }
    public LiveData<List<Bookmark>> getByDocumentName(String documentName) { return repo.getBookmarksByDocumentName(documentName);}
    public LiveData<List<Bookmark>> getByVideoName(String videoName) {return repo.getBookmarkByVideoName(videoName);}


    public LiveData<List<Bookmark>> orderByName(boolean ascending, String bookmarkName){ return repo.orderByName(ascending, bookmarkName);}
    public LiveData<List<Bookmark>> orderByDate(boolean ascending, String bookmarkName){ return repo.orderByDate(ascending,bookmarkName);}
    public LiveData<List<Bookmark>> orderByVideoName(boolean ascending, String bookmarkName){ return repo.orderByVideoName(ascending,bookmarkName);}
    public LiveData<List<Bookmark>> orderByDocumentName(boolean ascending, String bookmarkName){ return repo.orderByDocumentName(ascending,bookmarkName);}
    public void insert(Bookmark bookmark) { repo.insert(bookmark); }
    public void delete(Bookmark bookmark) { repo.delete(bookmark); }
    public void delete(int bk_id) {repo.delete(bk_id); }
    public void update(int bk_id, String documentName, Uri documentPath, int pageNumber) { repo.update(bk_id, documentName, documentPath, pageNumber);}
    public List<Bookmark> getByVideoTemporary(String videoName) { return repo.getByVideoTemporary(videoName);}
}
