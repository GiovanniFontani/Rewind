package com.example.rewind.bookmarking.database;

import android.net.Uri;

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

    @Query("UPDATE bookmark SET documentName =:documentName, documentPath =:documentPath, pageNumber =:pageNumber WHERE bk_id =:id")
    void update(int id, String documentName, Uri documentPath, int pageNumber);

    @Query("SELECT * FROM BOOKMARK ORDER BY CASE WHEN :ascending = 1 THEN name END DESC, CASE WHEN :ascending = 0 THEN name END ASC")
    LiveData<List<Bookmark>> getAllOrderedByName(boolean ascending);

    @Query("SELECT * FROM BOOKMARK ORDER BY CASE WHEN :ascending = 1 THEN date END ASC, CASE WHEN :ascending = 0 THEN date END DESC")
    LiveData<List<Bookmark>> getAllOrderedByDate(boolean ascending);

    @Query("SELECT * FROM BOOKMARK ORDER BY CASE WHEN :ascending = 1 THEN videoName END DESC, CASE WHEN :ascending = 0 THEN videoName END ASC")
    LiveData<List<Bookmark>> getAllOrderedByVideoName(boolean ascending);

    @Query("SELECT * FROM BOOKMARK ORDER BY CASE WHEN :ascending = 1 THEN documentName END DESC, CASE WHEN :ascending = 0 THEN documentName END ASC")
    LiveData<List<Bookmark>> getAllOrderedByDocumentName(boolean ascending);


    @Query("SELECT * FROM BOOKMARK WHERE name LIKE :bookmark_name ORDER BY CASE WHEN :ascending = 1 THEN name END DESC, CASE WHEN :ascending = 0 THEN name END ASC")
    LiveData<List<Bookmark>> getAllOrderedByName(boolean ascending, String bookmark_name);

    @Query("SELECT * FROM BOOKMARK WHERE name LIKE :bookmark_name ORDER BY CASE WHEN :ascending = 1 THEN date END ASC, CASE WHEN :ascending = 0 THEN date END DESC")
    LiveData<List<Bookmark>> getAllOrderedByDate(boolean ascending, String bookmark_name);

    @Query("SELECT * FROM BOOKMARK WHERE name LIKE :bookmark_name ORDER BY CASE WHEN :ascending = 1 THEN videoName END DESC, CASE WHEN :ascending = 0 THEN videoName END ASC")
    LiveData<List<Bookmark>> getAllOrderedByVideoName(boolean ascending, String bookmark_name);

    @Query("SELECT * FROM BOOKMARK WHERE name LIKE :bookmark_name ORDER BY CASE WHEN :ascending = 1 THEN documentName END DESC, CASE WHEN :ascending = 0 THEN documentName END ASC")
    LiveData<List<Bookmark>> getAllOrderedByDocumentName(boolean ascending, String bookmark_name);

    @Query ("SELECT * FROM bookmark WHERE videoName LIKE :videoName")
    List<Bookmark> getByVideoTemporary(String videoName);
}
