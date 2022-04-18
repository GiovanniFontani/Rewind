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
    private final TextView pageNumber;
    private final TextView videoTime;

    private VideoBookmarkViewHolder(View itemView) {
        super(itemView);
        nameItemView = itemView.findViewById(R.id.video_player_name_view);
        documentNameItemView = itemView.findViewById(R.id.video_player_document_name_view);
        dateItemView = itemView.findViewById(R.id.video_player_date_view);
        videoTime = itemView.findViewById(R.id.video_player_bookmark_time);
        pageNumber = itemView.findViewById(R.id.video_player_document_page_number_view);
    }

    public void bind(String name, String documentName, String date, String time, String page) {
        nameItemView.setText(name);
        documentNameItemView.setText(documentName);
        dateItemView.setText(date);
        videoTime.setText(time);
        if(Integer.parseInt(page) == 0)
            pageNumber.setText("---");
        else
            pageNumber.setText(page);
    }

    static VideoBookmarkViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_player_bookmark_layout, parent, false);
        return new VideoBookmarkViewHolder(view);
    }
}
