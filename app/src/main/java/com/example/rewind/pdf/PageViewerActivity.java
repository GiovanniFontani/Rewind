package com.example.rewind.pdf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rewind.R;
import com.github.barteksc.pdfviewer.PDFView;

public class PageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_viewer);

        PDFView pdfView = findViewById(R.id.page_viewer_pdf_view);
        String a = getIntent().getStringExtra("pdfUri");
        Uri pdfUri = Uri.parse(a);
        int pageNumber = Integer.parseInt(getIntent().getStringExtra("pageNumber")) ;
        pdfView.fromUri(pdfUri).defaultPage(pageNumber).load();
        final Button selectionButton = findViewById(R.id.select_pdf_page);

        selectionButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
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




