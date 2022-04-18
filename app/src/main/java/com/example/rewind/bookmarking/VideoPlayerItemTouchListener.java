package com.example.rewind.bookmarking;

import android.view.MotionEvent;
import android.view.View;

import com.example.rewind.bookmarking.database.Bookmark;

public interface VideoPlayerItemTouchListener{
    public void onTouch(View itemView, MotionEvent event, int selectedPosition, Bookmark current);
}
