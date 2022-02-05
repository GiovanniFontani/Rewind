package com.example.rewind.bookmarking;

import android.media.Image;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarkListContent {

    public static final List<BookmarkItem> ITEMS = new ArrayList<>();
    public static final Map<String,BookmarkItem> ITEM_MAP = new HashMap<>();

    public BookmarkListContent(){
        //TODO add viewModel interaction here.
    }

    protected static void addItem(BookmarkItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.name, item);
    }

    public static class BookmarkItem {
        public final String name;
        public final Date date;
        public final String documentName;

        public BookmarkItem(String name, String documentName, Date date, Image thumbnail){
            this.name = name;
            this.documentName = documentName;
            this.date = date;
        }

        @NonNull
        @Override
        public String toString(){
            return name + "-" + documentName + "-" + date.toString();
        }
    }
}

