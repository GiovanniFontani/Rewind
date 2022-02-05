package com.example.rewind.bookmarking.database;

import android.app.Application;

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

    public void insert(Bookmark bookmark) { repo.insert(bookmark); }
}