package com.example.rewind.bookmarking.database;

import android.media.Image;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Bookmark {

        @PrimaryKey(autoGenerate = true)
        public int bk_id;

        @ColumnInfo(name = "name")
        public String name;

        @ColumnInfo(name ="documentName")
        public String documentName;

        @ColumnInfo(name = "date")
        public Date date;

        //TODO: videoName column

        public Bookmark(int bk_id, String name, String documentName, Date date){
                this.bk_id = bk_id;
                this.name = name;
                this.documentName = documentName;
                this.date = date;
        }
        @Ignore
        public Bookmark(String name, String documentName, Date date){
                this.name = name;
                this.documentName = documentName;
                this.date = date;
        }
}
