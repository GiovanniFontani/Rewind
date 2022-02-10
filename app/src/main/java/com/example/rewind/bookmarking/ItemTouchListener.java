package com.example.rewind.bookmarking;

import android.view.MotionEvent;
import android.view.View;

public interface ItemTouchListener {
    public void onTouch(View view, MotionEvent motionEvent, int position);
}
