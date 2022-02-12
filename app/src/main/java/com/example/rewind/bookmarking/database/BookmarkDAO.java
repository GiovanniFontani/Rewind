package com.example.rewind.bookmarking.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookmarkDAO {
    @Query("SELECT * FROM bookmark")
    LiveData<List<Bookmark>> getAll();

    @Query("SELECT * FROM bookmark WHERE name IN (:bookmark_Ids)")
    LiveData<List<Bookmark>> loadAllByIds(int[] bookmark_Ids);

    @Query("SELECT * FROM bookmark WHERE bk_id LIKE :bk_id")
    Bookmark findById(String bk_id);

    @Insert
    void insertAll(Bookmark... bookmarks);

    @Delete
    void delete(Bookmark bookmark);

    @Query( "DELETE from bookmark WHERE bk_id LIKE(:bk_id)")
    void delete(int bk_id);

    @Query ("SELECT * FROM bookmark WHERE documentName LIKE :documentName")
    LiveData<List<Bookmark>> findByLinkedDocumentName(String documentName);

    @Query ("SELECT * FROM bookmark WHERE videoName LIKE :videoName")
    LiveData<List<Bookmark>> findByVideoName(String videoName);

    //TODO query ORDER BY
}
