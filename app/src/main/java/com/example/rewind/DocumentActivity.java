package com.example.rewind;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class DocumentActivity extends AppCompatActivity {

    String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        PDFView pdfView = findViewById(R.id.pdfView);
        filePath = getIntent().getStringExtra("path");
        File pdfFile = new File(filePath);
        Uri path = Uri.fromFile(pdfFile);
        pdfView.fromUri(path).load();

        final Button selectionButton = findViewById(R.id.select_pdf_button);
        selectionButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            replyIntent.putExtra("pdfFile",pdfFile);
            setResult(RESULT_OK, replyIntent);
            finish();
        });

        final Button backButton = findViewById(R.id.back_pdf_reader_button);
        backButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            setResult(RESULT_CANCELED, replyIntent);
            finish();
        });
    }

}