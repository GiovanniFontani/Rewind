package com.example.rewind.bookmarking;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rewind.PDFReader;
import com.example.rewind.PageViewerActivity;
import com.example.rewind.R;
import com.example.rewind.bookmarking.database.BookmarkViewModel;

import java.io.File;

/**
 * A fragment representing a list of bookmarks.
 */
public class BookmarkFragment extends Fragment implements ItemTouchListener {
    private BookmarkViewModel bookmarkViewModel;
    private ActivityResultLauncher<Intent> launcherImageView;
    public BookmarkFragment() {

    }

    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"ClickableViewAccessibility", "LongLogTag"})
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
            bookmarkViewModel = new ViewModelProvider(requireActivity()).get(BookmarkViewModel.class);
            bookmarkViewModel.getAll().observe(getViewLifecycleOwner(), adapter::submitList);
            ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            Uri pdfUri = Uri.parse(data.getStringExtra("pdfUri"));
                            Toast pdfPath_caught  = Toast.makeText(getActivity(),pdfUri.toString(), Toast.LENGTH_SHORT);
                            pdfPath_caught.show();
                            File pdfFile = new File(pdfUri.getPath());
                            String fileName = pdfUri.toString();
                            int lastSlashIndex = fileName.lastIndexOf("/");
                            fileName = fileName.substring(lastSlashIndex + 1, fileName.length());
                            Log.d("Pdf Name", pdfFile.getName());
                            bookmarkViewModel.update(adapter.getSelectedPositionBookmark().bk_id,fileName,pdfUri,Integer.parseInt(data.getStringExtra("page")));
                            Log.d("Pdf Page", data.getStringExtra("page"));
                        }
                    });
            launcherImageView = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            bookmarkViewModel.update(adapter.getSelectedPositionBookmark().bk_id,adapter.getSelectedPositionBookmark().documentName,adapter.getSelectedPositionBookmark().documentPath,Integer.parseInt(data.getStringExtra("page")));
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
                    if(adapter.getSelectedPosition() == 0) {
                        getActivity().findViewById(R.id.select_pdf_for_bookmark_button).setEnabled(false);
                        getActivity().findViewById(R.id.delete_bookmark_button).setEnabled(false);
                    }
                }else{
                    Toast errorDelete = Toast.makeText(getActivity(),"Select a bookmark first!", Toast.LENGTH_SHORT);
                    errorDelete.show();
                }
            });
        }
        return bookmarkView;
    }

    @Override
    public void onImageViewTouch(View view, MotionEvent motionEvent, Uri pdfUri, int pageNumber) {
        Intent intent = new Intent(this.getActivity(), PageViewerActivity.class).putExtra("pdfUri", pdfUri.toString());
        intent.putExtra("pageNumber", Integer.toString(pageNumber));
        launcherImageView.launch(intent);
    }

    @Override
    public void onTouch(View view, MotionEvent motionEvent, int position) {
        getActivity().findViewById(R.id.select_pdf_for_bookmark_button).setEnabled(true);
        getActivity().findViewById(R.id.delete_bookmark_button).setEnabled(true);
    }
}