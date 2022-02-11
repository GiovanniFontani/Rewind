package com.example.rewind;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class PDFAdapter extends RecyclerView.Adapter<PDFViewHolder> {
    private Context context;
    private List<File> pdfFiles;
    private OnPDFFileSelectListener listener;


    public PDFAdapter(Context context, List<File> pdfFiles, OnPDFFileSelectListener listener) {
        this.context = context;
        this.pdfFiles = pdfFiles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PDFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PDFViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_pdf_holder, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull PDFViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvName.setText(pdfFiles.get(position).getName());
        holder.tvName.setSelected(true);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPdfSelected(pdfFiles.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }
}
