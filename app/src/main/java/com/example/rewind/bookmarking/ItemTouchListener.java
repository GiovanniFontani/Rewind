package com.example.rewind.bookmarking;

import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;

public interface ItemTouchListener {
    public void onTouch(View view, MotionEvent motionEvent, int position);
    public void onImageViewTouch(View view, MotionEvent motionEvent, Uri uriPdf, int pageNumber);
}
