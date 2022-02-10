package com.example.rewind.bookmarking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.animation.content.Content;
import com.example.rewind.PDFReader;
import com.example.rewind.R;
import com.example.rewind.bookmarking.database.BookmarkViewModel;
import com.google.android.material.button.MaterialButton;

import java.io.File;

/**
 * A fragment representing a list of bookmarks.
 */
public class BookmarkFragment extends Fragment implements ItemTouchListener {
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View bookmarkView = inflater.inflate(R.layout.fragment_item_list, container, false);
        RecyclerView recycler = bookmarkView.findViewById(R.id.recyclerview);
        if (recycler != null) {
            Context context = recycler.getContext();
            recycler.setLayoutManager(new LinearLayoutManager(context));
            final BookmarkListAdapter adapter = new BookmarkListAdapter(new BookmarkListAdapter.BookmarkDiff());
            adapter.setClickListener(this);
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
            ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            File pdfFile = new File(data.getStringExtra("pdfFile"));
                        }
                    });
            bookmarkView.findViewById(R.id.select_pdf_for_bookmark_button).setOnClickListener(view -> {
                if(adapter.isRowSelected()) {
                    Intent intent = new Intent(this.getActivity(), PDFReader.class);
                    launcher.launch(intent);
                }else{
                    Toast errorSelectedPdf = Toast.makeText(getActivity(),"Select a bookmark first!", Toast.LENGTH_SHORT);
                    errorSelectedPdf.show();
                }
            });
            bookmarkView.findViewById(R.id.delete_bookmark_button).setOnClickListener(view -> {
                if(adapter.isRowSelected()) {
                    bookmarkViewModel.delete(adapter.getSelectedPositionBookmark());
                }else{
                    Toast errorDelete = Toast.makeText(getActivity(),"Select a bookmark first!", Toast.LENGTH_SHORT);
                    errorDelete.show();
                }
            });

        }
        return bookmarkView;
    }


    @Override
    public void onTouch(View view, MotionEvent motionEvent, int position) {
        getActivity().findViewById(R.id.select_pdf_for_bookmark_button).setEnabled(true);
        getActivity().findViewById(R.id.delete_bookmark_button).setEnabled(true);
    }
}