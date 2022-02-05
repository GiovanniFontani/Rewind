package com.example.rewind.bookmarking;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rewind.MainActivity;
import com.example.rewind.NewBookmarkActivity;
import com.example.rewind.R;
import com.example.rewind.VideoPlayerFragment;
import com.example.rewind.bookmarking.database.Bookmark;
import com.example.rewind.bookmarking.database.BookmarkViewModel;

import java.util.Calendar;

/**
 * A fragment representing a list of bookmarks.
 */
public class BookmarkFragment extends Fragment {
    private BookmarkViewModel bookmarkViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public BookmarkFragment() {

    }

    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View bookmarkView = inflater.inflate(R.layout.fragment_item_list, container, false);
        RecyclerView recycler = bookmarkView.findViewById(R.id.recyclerview);
        if (recycler != null) {
            Context context = recycler.getContext();;
            recycler.setLayoutManager(new LinearLayoutManager(context));
            final BookmarkListAdapter adapter = new BookmarkListAdapter(new BookmarkListAdapter.BookmarkDiff());
            recycler.setAdapter(adapter);
            bookmarkViewModel = new ViewModelProvider(this).get(BookmarkViewModel.class);
            bookmarkViewModel.getAll().observe(getViewLifecycleOwner(), adapter::submitList);
            bookmarkView.findViewById(R.id.nav_from_bookmark_to_intro).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavHostFragment.findNavController(BookmarkFragment.this)
                            .navigate(R.id.action_bookmarkFragment_to_introFragment);
                }
            });
            bookmarkView.findViewById(R.id.add_bookmark).setOnClickListener( view -> {
            //TODO: add activity launch call HERE
                });
        }

        return bookmarkView;
    }
}