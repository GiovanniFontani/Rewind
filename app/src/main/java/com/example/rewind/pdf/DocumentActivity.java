package com.example.rewind.pdf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rewind.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class DocumentActivity extends AppCompatActivity {

    String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        PDFView pdfView = findViewById(R.id.page_viewer_pdf_view);
        filePath = getIntent().getStringExtra("path");
        File pdfFile = new File(filePath);
        Uri pdfUri = Uri.fromFile(pdfFile);

        pdfView.fromUri(pdfUri).load();
        final Button selectionButton = findViewById(R.id.select_pdf_page);
        selectionButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            replyIntent.putExtra("pdfUri",pdfUri.toString());
            replyIntent.putExtra("page", Integer.toString(pdfView.getCurrentPage()));
            setResult(RESULT_OK, replyIntent);
            finish();
        });

        final Button backButton = findViewById(R.id.back_page_viewer_button);
        backButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            setResult(RESULT_CANCELED, replyIntent);
            finish();
        });
    }

}