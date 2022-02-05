package com.example.rewind.bookmarking;

import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rewind.R;

import java.util.Date;

public class BookmarkViewHolder extends RecyclerView.ViewHolder{

    private final TextView nameItemView;
    private final TextView documentNameItemView;
    private final TextView dateItemView;

    private BookmarkViewHolder(View itemView) {
        super(itemView);
        nameItemView = itemView.findViewById(R.id.nameView);
        documentNameItemView = itemView.findViewById(R.id.documentNameView);
        dateItemView = itemView.findViewById(R.id.dateView);
    }

    public void bind(String name, String documentName, Date date) {
        nameItemView.setText(name);
        documentNameItemView.setText(documentName);
        dateItemView.setText(date.toString());
    }

    static BookmarkViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bookmark, parent, false);
        return new BookmarkViewHolder(view);
    }
}
