package com.example.rewind.bookmarking;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rewind.PDFReader;
import com.example.rewind.PageViewerActivity;
import com.example.rewind.R;
import com.example.rewind.bookmarking.database.BookmarkViewModel;

import java.io.File;

/**
 * A fragment representing a list of bookmarks.
 */
public class BookmarkFragment extends Fragment implements ItemTouchListener, AdapterView.OnItemSelectedListener {
    private BookmarkViewModel bookmarkViewModel;
    private ActivityResultLauncher<Intent> launcherImageView;
    private BookmarkListAdapter adapter;
    private Button select_pdf_button;
    private Button delete_button;
    private View fragmentView;
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
        fragmentView = inflater.inflate(R.layout.fragment_item_list, container, false);
        select_pdf_button = fragmentView.findViewById(R.id.select_pdf_for_bookmark_button);
        delete_button = fragmentView.findViewById(R.id.delete_bookmark_button);
        RecyclerView recycler = fragmentView.findViewById(R.id.recyclerview);
        if (recycler != null) {
            Context context = recycler.getContext();
            recycler.setLayoutManager(new LinearLayoutManager(context));
            adapter = new BookmarkListAdapter(new BookmarkListAdapter.BookmarkDiff());
            adapter.setClickListener(this);
            recycler.setAdapter(adapter);
            bookmarkViewModel = new ViewModelProvider(requireActivity()).get(BookmarkViewModel.class);
            ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            Uri pdfUri = Uri.parse(data.getStringExtra("pdfUri"));
                            String fileName = pdfUri.toString();
                            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                            bookmarkViewModel.update(adapter.getSelectedPositionBookmark().bk_id,fileName,pdfUri,Integer.parseInt(data.getStringExtra("page")));
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
            select_pdf_button.setOnClickListener(view -> {
                if(adapter.isRowSelected()) {
                    Intent intent = new Intent(this.getActivity(), PDFReader.class);
                    launcher.launch(intent);
                }else{
                    Toast errorSelectedPdf = Toast.makeText(getActivity(),"Select a bookmark first!", Toast.LENGTH_SHORT);
                    errorSelectedPdf.show();
                }
            });
            delete_button.setOnClickListener(view -> {
                if(adapter.isRowSelected()) {
                    bookmarkViewModel.delete(adapter.getSelectedPositionBookmark());
                    if(adapter.getSelectedPosition() == 0) {
                        select_pdf_button.setEnabled(false);
                        delete_button.setEnabled(false);
                    }
                }else{
                    Toast errorDelete = Toast.makeText(getActivity(),"Select a bookmark first!", Toast.LENGTH_SHORT);
                    errorDelete.show();
                }
            });
        }

        Spinner spinner = fragmentView.findViewById(R.id.search_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.search_spinner_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        TextView search_bar = fragmentView.findViewById(R.id.search_bookmark_text_bar);
        search_bar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action_id, KeyEvent keyEvent) {
                if(action_id == EditorInfo.IME_ACTION_DONE){
                    String selection = ((Spinner)fragmentView.findViewById(R.id.search_spinner)).getSelectedItem().toString();
                    updateList(selection);
                    return true;
                }else{
                    return false;
                }
            }
        });

        return fragmentView;
    }

    @Override
    public void onImageViewTouch(View view, MotionEvent motionEvent, Uri pdfUri, int pageNumber) {
        Intent intent = new Intent(this.getActivity(), PageViewerActivity.class).putExtra("pdfUri", pdfUri.toString());
        intent.putExtra("pageNumber", Integer.toString(pageNumber));
        launcherImageView.launch(intent);
    }

    @Override
    public void onTouch(View view, MotionEvent motionEvent, int position) {
        select_pdf_button.setEnabled(true);
        delete_button.setEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String selection = parent.getItemAtPosition(pos).toString();
        updateList(selection);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void updateList(String selection){
        String searchText =((EditText)fragmentView.findViewById(R.id.search_bookmark_text_bar)).getText().toString();
        bookmarkViewModel = new ViewModelProvider(requireActivity()).get(BookmarkViewModel.class);
        switch(selection) {
            case ("by Name (Z-A)"):
                bookmarkViewModel.orderByName(true, searchText).observe(getViewLifecycleOwner(), adapter::submitList);
                break;
            case ("by Name (A-Z)"):
                bookmarkViewModel.orderByName(false,searchText).observe(getViewLifecycleOwner(), adapter::submitList);
                break;
            case("by Date (latest)"):
                bookmarkViewModel.orderByDate(false,searchText).observe(getViewLifecycleOwner(), adapter::submitList);
                break;
            case("by Date (oldest)"):
                bookmarkViewModel.orderByDate(true,searchText).observe(getViewLifecycleOwner(), adapter::submitList);
                break;
            case("by Video Name (Z-A)"):
                bookmarkViewModel.orderByVideoName(true,searchText).observe(getViewLifecycleOwner(), adapter::submitList);
                break;
            case("by Video Name (A-Z)"):
                bookmarkViewModel.orderByVideoName(false,searchText).observe(getViewLifecycleOwner(), adapter::submitList);
                break;
        }
    }

    //TODO: scoprire perchè, a volte, cliccando le immaginine thumbnail, si scazzano tutte le altre
    // non sembra essere un problema di update della lista, ma più un problema di caching o magia nera di Android.
}