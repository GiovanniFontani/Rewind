package com.example.rewind.bookmarking;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rewind.R;
import com.example.rewind.audio.Boombox;
import com.example.rewind.pdf.PDFReader;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.IOException;

public class NewBookmarkActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private EditText mEditWordView;
    private TextView documentName;
    private TextView documentPath;
    private TextView documentPage;
    private ImageView pdfThumbnail;
    private String filePath;
    private String fileName;
    private String pageNumber;
    private Button select_pdf_button;
    private Button save_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bookmark);
        mEditWordView = findViewById(R.id.edit_word);
        documentName = findViewById(R.id.pdf_document_name);
        documentPath = findViewById(R.id.pdf_document_path);
        documentPage = findViewById(R.id.pdf_document_bookmark_page);
        pdfThumbnail = findViewById(R.id.new_bookmark_thumbnail);
        select_pdf_button = findViewById(R.id.new_bookmark_select_pdf_button);
        save_button = findViewById(R.id.button_save);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Uri pdfUri = Uri.parse(data.getStringExtra("pdfUri"));
                        filePath = pdfUri.toString();
                        documentPath.setText(filePath);
                        documentPath.setTextColor(getResources().getColor(R.color.white));
                        documentPath.setSelected(true);
                        fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                        documentName.setText(fileName);
                        documentName.setTextColor(getResources().getColor(R.color.white));
                        documentName.setSelected(true);
                        pageNumber= data.getStringExtra("page") ;
                        documentPage.setText(Integer.toString(Integer.parseInt(pageNumber)+1));
                        documentPage.setTextColor(getResources().getColor(R.color.white));

                        if(pdfUri != null) {
                            File pdf = new File(pdfUri.getPath());
                            try {
                                PdfiumCore pdfiumCore = new PdfiumCore(pdfThumbnail.getContext());
                                ParcelFileDescriptor fd = pdfThumbnail.getContext().getContentResolver().openFileDescriptor(pdfUri, "r");
                                PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
                                pdfiumCore.openPage(pdfDocument, Integer.parseInt(pageNumber));
                                int width = pdfiumCore.getPageWidthPoint(pdfDocument, Integer.parseInt(pageNumber));
                                int height = pdfiumCore.getPageHeightPoint(pdfDocument, Integer.parseInt(pageNumber));
                                Bitmap bitmap = Bitmap.createBitmap(width, height,
                                        Bitmap.Config.RGB_565);
                                pdfiumCore.renderPageBitmap(pdfDocument, bitmap, Integer.parseInt(pageNumber), 0, 0,
                                        width, height);
                                pdfThumbnail.setImageBitmap(bitmap);
                                pdfiumCore.closeDocument(pdfDocument); // important!
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }else pdfThumbnail.setImageResource(R.drawable.ic_missing_pdf_icon);
                    }
                });
        select_pdf_button.setOnClickListener(view -> {
                Intent intent = new Intent(this, PDFReader.class);
                launcher.launch(intent);
        });
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(mEditWordView.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String bookmark_name = mEditWordView.getText().toString();
                replyIntent.putExtra(EXTRA_REPLY, bookmark_name);
                if(documentPath.getText()!= "") {
                    replyIntent.putExtra("documentPath", filePath);
                    replyIntent.putExtra("documentName", fileName);
                    replyIntent.putExtra("documentPage", pageNumber);
                } else{
                    replyIntent.putExtra("documentPath","null");
                    replyIntent.putExtra("documentName","null");
                    replyIntent.putExtra("documentPage","null");
                }
                setResult(RESULT_OK, replyIntent);
            }
            Boombox.getInstance().play(R.raw.save_sound, getApplicationContext());
            finish();
        });
        mEditWordView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Log.d("Valore editable", String.valueOf(s));
                if(String.valueOf(s).equals(""))
                    save_button.setEnabled(false);
                else
                    save_button.setEnabled(true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

}