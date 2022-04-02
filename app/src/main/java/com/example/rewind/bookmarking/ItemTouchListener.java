package com.example.rewind.bookmarking;

import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;

import com.example.rewind.bookmarking.database.Bookmark;

public interface ItemTouchListener {
    public void onTouch(View itemView, MotionEvent event, int selectedPosition, Bookmark current);
    public void onImageViewTouch(View view, MotionEvent motionEvent, Uri uriPdf, int pageNumber);
}
