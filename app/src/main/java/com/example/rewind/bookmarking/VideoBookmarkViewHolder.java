package com.example.rewind.bookmarking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rewind.R;

public class VideoBookmarkViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameItemView;
    private final TextView documentNameItemView;
    private final TextView dateItemView;

    private VideoBookmarkViewHolder(View itemView) {
        super(itemView);
        nameItemView = itemView.findViewById(R.id.nameView);
        documentNameItemView = itemView.findViewById(R.id.documentNameView);
        dateItemView = itemView.findViewById(R.id.dateView);
    }

    public void bind(String name, String documentName, String date) {
        nameItemView.setText(name);
        documentNameItemView.setText(documentName);
        dateItemView.setText(date);
    }

    static VideoBookmarkViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_player_bookmark_layout, parent, false);
        return new VideoBookmarkViewHolder(view);
    }
}
