package com.example.rewind;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.rewind.bookmarking.database.Bookmark;
import com.example.rewind.bookmarking.database.DateGetter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PDFReader extends AppCompatActivity implements OnPDFFileSelectListener {
    private PDFAdapter pdfAdapter;
    private List<File> pdfList;
    private RecyclerView recyclerView;
    private ActivityResultLauncher<Intent> launcher;
    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfreader);
        runtimePermission();
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        File pdfFile = new File(data.getStringExtra("pdfFile"));
                        Intent replyIntent = new Intent();
                        replyIntent.putExtra("pdfFile",pdfFile);
                        setResult(RESULT_OK, replyIntent);
                        finish();
                    }
                });
    }

    private void runtimePermission(){
        Dexter.withContext(PDFReader.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                displayPdf();
            }
            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(PDFReader.this, "Permission is Required!", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> findPdf(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null){
            for(File singleFile: files){
                if(singleFile.isDirectory() && !singleFile.isHidden()){
                    arrayList.addAll(findPdf(singleFile));
                }
                else{
                    if (singleFile.getName().endsWith(".pdf")) {
                        arrayList.add(singleFile);
                    }
                }
            }
        }
        return arrayList;
    }

    private void displayPdf() {
        recyclerView = findViewById(R.id.recycler_view_pdf);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager( this,  3));
        pdfList = new ArrayList<>();
        //   File aa = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //   File externalStorage = Environment.getExternalStorageDirectory();
        pdfList.addAll(findPdf(new File(Environment.getExternalStorageDirectory().getAbsolutePath())));
        pdfAdapter = new PDFAdapter(this, pdfList, this);
        recyclerView.setAdapter(pdfAdapter);
    }

    @Override
    public void onPdfSelected(File file) {
        Intent intent = new Intent(this, DocumentActivity.class).putExtra("path", file.getAbsolutePath());
        launcher.launch(intent);
    }
}