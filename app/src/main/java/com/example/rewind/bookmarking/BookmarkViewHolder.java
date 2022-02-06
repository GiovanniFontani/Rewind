package com.example.rewind.bookmarking;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rewind.R;
import java.util.Date;

public class BookmarkViewHolder extends RecyclerView.ViewHolder{

    private final TextView nameItemView;
    private final TextView documentNameItemView;
    private final TextView dateItemView;
    private final TextView idView;

    private BookmarkViewHolder(View itemView) {
        super(itemView);
        nameItemView = itemView.findViewById(R.id.nameView);
        documentNameItemView = itemView.findViewById(R.id.documentNameView);
        dateItemView = itemView.findViewById(R.id.dateView);
        idView = itemView.findViewById(R.id.hidden_id);
    }

    public void bind(String name, String documentName, Date date, String id) {
        nameItemView.setText(name);
        documentNameItemView.setText(documentName);
        dateItemView.setText(date.toString());
        idView.setText(id);
    }

    static BookmarkViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bookmark, parent, false);
        return new BookmarkViewHolder(view);
    }
}
