package com.example.rewind.bookmarking;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.rewind.R;
import com.example.rewind.bookmarking.database.Bookmark;
import com.example.rewind.bookmarking.database.DateGetter;

public class VideoBookmarkListAdapter extends ListAdapter<Bookmark, VideoBookmarkViewHolder> {
    private int selectedPosition=-1;
    private VideoPlayerItemTouchListener clickListener;

    public VideoBookmarkListAdapter(@NonNull DiffUtil.ItemCallback<Bookmark> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public VideoBookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return VideoBookmarkViewHolder.create(parent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(VideoBookmarkViewHolder holder, int position) {
        Bookmark current = getItem(position);
        holder.bind(current.name,current.documentName, DateGetter.formatDate(current.date), DateGetter.LocalTimeToString(current.videoTime), Integer.toString(current.pageNumber + 1));
        if(selectedPosition == position) {
            //setSelected(true) is for marquee forever when selected
            holder.itemView.setBackgroundResource(R.drawable.video_player_bookmark_selected);
            holder.itemView.findViewById(R.id.video_player_name_view).setSelected(true);
            holder.itemView.findViewById(R.id.video_player_document_name_view).setSelected(true);
            holder.itemView.findViewById(R.id.video_player_date_view).setSelected(true);
            holder.itemView.findViewById(R.id.video_player_document_page_view).setSelected(true);
        }else{
            holder.itemView.setBackgroundResource(R.drawable.video_player_bookmark);
            holder.itemView.findViewById(R.id.video_player_name_view).setSelected(false);
            holder.itemView.findViewById(R.id.video_player_document_name_view).setSelected(false);
            holder.itemView.findViewById(R.id.video_player_date_view).setSelected(false);
            holder.itemView.findViewById(R.id.video_player_document_page_number_view).setSelected(false);
        }
        holder.itemView.setOnClickListener(v -> {
            selectedPosition=holder.getAbsoluteAdapterPosition();
            if(selectedPosition != -1) {
                MotionEvent event = null;
                clickListener.onTouch(holder.itemView, event, selectedPosition, current);
            }
            notifyDataSetChanged();
        });
    }

    public Bookmark getSelectedPositionBookmark() {
        return getItem(selectedPosition);
    }

    public boolean isRowSelected() {return selectedPosition != -1;}

    public void refresh(){
        notifyDataSetChanged();
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

    public void setClickListener(VideoPlayerItemTouchListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
