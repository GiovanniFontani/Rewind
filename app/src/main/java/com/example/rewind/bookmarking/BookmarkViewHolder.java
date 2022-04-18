package com.example.rewind.bookmarking;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rewind.R;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.IOException;

public class BookmarkViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameItemView;
    private final TextView documentNameItemView;
    private final TextView dateItemView;
    private final TextView idView;
    private final TextView videoNameView;
    private final TextView videoTime;
    private final ImageView pdfThumbnail;

    private BookmarkViewHolder(View itemView) {
        super(itemView);
        nameItemView = itemView.findViewById(R.id.nameView);
        documentNameItemView = itemView.findViewById(R.id.documentNameView);
        dateItemView = itemView.findViewById(R.id.dateView);
        idView = itemView.findViewById(R.id.hidden_id_view);
        videoNameView = itemView.findViewById(R.id.videoNameView);
        pdfThumbnail = itemView.findViewById(R.id.page_viewer_pdf_view);
        videoTime = itemView.findViewById(R.id.videoTime);
    }

    public void bind(String name, String documentName, String date, String id, String videoname, Uri uri, int pageNumber,String time) {
        nameItemView.setText(name);
        documentNameItemView.setText(documentName);
        dateItemView.setText(date);
        idView.setText(id);
        videoNameView.setText(videoname);
        videoTime.setText(time);
        if(uri != null) {
            File pdf = new File(uri.getPath());
                try {
                    PdfiumCore pdfiumCore = new PdfiumCore(pdfThumbnail.getContext());
                    ParcelFileDescriptor fd = pdfThumbnail.getContext().getContentResolver().openFileDescriptor(uri, "r");
                    PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
                    pdfiumCore.openPage(pdfDocument, pageNumber);
                    int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
                    int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
                    // ARGB_8888 - best quality, high memory usage, higher possibility of OutOfMemoryError
                    // RGB_565 - little worse quality, twice less memory usage
                    Bitmap bitmap = Bitmap.createBitmap(width, height,
                            Bitmap.Config.RGB_565);
                    pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNumber, 0, 0,
                            width, height);
                    //if you need to render annotations and form fields, you can use
                    //the same method above adding 'true' as last param
                    pdfThumbnail.setImageBitmap(bitmap);
                    pdfiumCore.closeDocument(pdfDocument); // important!
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }else pdfThumbnail.setImageResource(R.drawable.ic_missing_pdf_icon);
    }


        static BookmarkViewHolder create(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_bookmark, parent, false);
            return new BookmarkViewHolder(view);
        }

    }