package com.example.rewind.bookmarking.database;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Bookmark {

        @PrimaryKey(autoGenerate = true)
        public int bk_id;

        @ColumnInfo(name = "name")
        public String name;

        @ColumnInfo(name ="documentName")
        public String documentName;

        @ColumnInfo(name = "date")
        public String date;

        @ColumnInfo(name = "videoName")
        public String videoName;

        @ColumnInfo(name = "documentPath")
        public Uri documentPath;

        @ColumnInfo(name = "pageNumber")
        public int pageNumber;

        public Bookmark(int bk_id, String name, String documentName, String date, String videoName, Uri documentPath, int pageNumber){
                this.bk_id = bk_id;
                this.name = name;
                this.documentName = documentName;
                this.date = date;
                this.videoName=videoName;
                this.documentPath=documentPath;
                this.pageNumber=pageNumber;
        }
        @Ignore
        public Bookmark(String name, String documentName, String date,String videoName,Uri documentPath, int pageNumber){
                this.name = name;
                this.documentName = documentName;
                this.date = date;
                this.videoName=videoName;
                this.documentPath=documentPath;
                this.pageNumber=pageNumber;
        }
}
