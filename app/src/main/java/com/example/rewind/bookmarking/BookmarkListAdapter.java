package com.example.rewind.bookmarking;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.animation.content.Content;
import com.example.rewind.R;
import com.example.rewind.bookmarking.database.Bookmark;

public class BookmarkListAdapter extends ListAdapter<Bookmark, BookmarkViewHolder> {
    private int selectedPosition=-1;
    private ItemTouchListener clickListener;

    public BookmarkListAdapter(@NonNull DiffUtil.ItemCallback<Bookmark> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BookmarkViewHolder.create(parent);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, int position) {
        Bookmark current = getItem(position);
        holder.bind(current.name,current.documentName,current.date, Integer.toString(current.bk_id), current.videoName);
        if(selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.selected_bookmark_layout_border); //TODO: change color to default theme
        }else{
            holder.itemView.setBackgroundResource(R.drawable.bookmark_layout_border); //TODO: change color to default theme
        }
        holder.itemView.setOnClickListener(v -> {
            selectedPosition=holder.getAbsoluteAdapterPosition();
            if(selectedPosition != -1) {
                MotionEvent event = null;
                clickListener.onTouch(holder.itemView, event, selectedPosition);
            }
            notifyDataSetChanged();
        });
    }


    public Bookmark getSelectedPositionBookmark() {
        return getItem(selectedPosition);
    }


    public boolean isRowSelected(){ return selectedPosition != -1;}


    static class BookmarkDiff extends DiffUtil.ItemCallback<Bookmark> {

        @Override
        public boolean areItemsTheSame(@NonNull Bookmark oldItem, @NonNull Bookmark newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Bookmark oldItem, @NonNull Bookmark newItem) {
            return  oldItem.bk_id == newItem.bk_id
                    && oldItem.name.equals(newItem.name)
                    && oldItem.documentName.equals(newItem.documentName)
                    && oldItem.date.equals(newItem.date)
                    && oldItem.videoName.equals(newItem.videoName);
        }
    }

    public void setClickListener(ItemTouchListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}
