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

    @Query("SELECT * FROM bookmark WHERE name IN (:userIds)")
    LiveData<List<Bookmark>> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM bookmark WHERE bk_id LIKE :bk_id")
    Bookmark findById(String bk_id);

    @Insert
    void insertAll(Bookmark... users);

    @Delete
    void delete(Bookmark user);
}
