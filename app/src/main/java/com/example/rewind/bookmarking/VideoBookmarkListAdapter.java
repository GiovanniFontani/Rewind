package com.example.rewind.bookmarking;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.rewind.R;
import com.example.rewind.bookmarking.database.Bookmark;

public class VideoBookmarkListAdapter extends ListAdapter<Bookmark, VideoBookmarkViewHolder> {
    private int selectedPosition=-1;
    public VideoBookmarkListAdapter(@NonNull DiffUtil.ItemCallback<Bookmark> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public VideoBookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return VideoBookmarkViewHolder.create(parent);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(VideoBookmarkViewHolder holder, int position) {
        Bookmark current = getItem(position);
        holder.bind(current.name,current.documentName,current.date);
        if(selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.video_player_bookmark_selected);
        }else{
            holder.itemView.setBackgroundResource(R.drawable.video_player_bookmark);
        }
        holder.itemView.setOnClickListener(v -> {
            selectedPosition=holder.getAbsoluteAdapterPosition();
            notifyDataSetChanged();
        });
    }

    public Bookmark getSelectedPositionBookmark() {
        return getItem(selectedPosition);
    }
    public static class BookmarkDiff extends DiffUtil.ItemCallback<Bookmark> {

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
}
