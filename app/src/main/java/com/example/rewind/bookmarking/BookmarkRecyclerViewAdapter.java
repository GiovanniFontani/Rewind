package com.example.rewind.bookmarking;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rewind.bookmarking.BookmarkListContent.BookmarkItem;
import com.example.rewind.databinding.FragmentBookmarkBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link BookmarkItem}.
 */
public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookmarkRecyclerViewAdapter.ViewHolder> {

    private final List<BookmarkItem> mValues;

    public BookmarkRecyclerViewAdapter(List<BookmarkItem> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentBookmarkBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).name);
        holder.mDocumentNameView.setText(mValues.get(position).documentName);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mNameView;
        public final TextView mDocumentNameView;
        public final TextView mDateView;
        public BookmarkItem mItem;

        public ViewHolder(FragmentBookmarkBinding binding) {
            super(binding.getRoot());
            mNameView = binding.nameView;
            mDocumentNameView = binding.documentNameView;
            mDateView = binding.dateView;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'" + mDocumentNameView.getText() + "'" + mDateView.getText();
        }
    }
}