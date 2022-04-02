package com.example.rewind.bookmarking;

import android.annotation.SuppressLint;
import android.net.Uri;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, int position) {
        Bookmark current = getItem(position);
        holder.bind(current.name,current.documentName, DateGetter.formatDate(current.date), Integer.toString(current.bk_id), current.videoName, current.documentPath, current.pageNumber, DateGetter.LocalTimeToString(current.videoTime));
        if(selectedPosition == position) {
            holder.itemView.setBackgroundResource(R.drawable.selected_bookmark_layout_border);
            holder.itemView.findViewById(R.id.nameView).setSelected(true);
            holder.itemView.findViewById(R.id.videoNameView).setSelected(true);
            holder.itemView.findViewById(R.id.documentNameView).setSelected(true);
        }else{
            holder.itemView.setBackgroundResource(R.drawable.bookmark_layout_border);
            holder.itemView.findViewById(R.id.nameView).setSelected(false);
            holder.itemView.findViewById(R.id.videoNameView).setSelected(false);
            holder.itemView.findViewById(R.id.documentNameView).setSelected(false);

        }
        holder.itemView.setOnClickListener(v -> {
            selectedPosition=holder.getAbsoluteAdapterPosition();
            if(selectedPosition != -1) {
                MotionEvent event = null;
                clickListener.onTouch(holder.itemView, event, selectedPosition, current);
            }
            notifyDataSetChanged();
        });
        holder.itemView.findViewById(R.id.page_viewer_pdf_view).setOnClickListener(v ->{
            MotionEvent event = null;
            selectedPosition = holder.getAbsoluteAdapterPosition();
            clickListener.onTouch(holder.itemView, event, selectedPosition,current);
            if(current.documentPath != null) {
                Uri a = current.documentPath;
                                clickListener.onImageViewTouch(holder.itemView, event, current.documentPath, current.pageNumber);
            }
            notifyDataSetChanged();
        });
    }


    public Bookmark getSelectedPositionBookmark() {
        return getItem(selectedPosition);
    }
    public int getSelectedPosition() { return selectedPosition;}
    public boolean isRowSelected(){ return selectedPosition != -1;}


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

    public void setClickListener(ItemTouchListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}
