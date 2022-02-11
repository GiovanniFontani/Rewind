package com.example.rewind.bookmarking;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rewind.R;
import java.util.Date;

public class BookmarkViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameItemView;
    private final TextView documentNameItemView;
    private final TextView dateItemView;
    private final TextView idView;
    private final TextView videoNameView;

    private BookmarkViewHolder(View itemView) {
        super(itemView);
        nameItemView = itemView.findViewById(R.id.nameView);
        documentNameItemView = itemView.findViewById(R.id.documentNameView);
        dateItemView = itemView.findViewById(R.id.dateView);
        idView = itemView.findViewById(R.id.hidden_id_view);
        videoNameView = itemView.findViewById(R.id.videoNameView);
    }

    public void bind(String name, String documentName, String date, String id, String videoname) {
        nameItemView.setText(name);
        documentNameItemView.setText(documentName);
        dateItemView.setText(date);
        idView.setText(id);
        videoNameView.setText(videoname);
    }

    static BookmarkViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bookmark, parent, false);
        return new BookmarkViewHolder(view);
    }
}
