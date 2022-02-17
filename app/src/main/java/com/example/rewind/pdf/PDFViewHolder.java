package com.example.rewind.pdf;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rewind.R;

public class PDFViewHolder extends RecyclerView.ViewHolder {
    public TextView tvName;
    public CardView container;
    public PDFViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.textPdfName);
        container = itemView.findViewById(R.id.container_pdf);
    }
}
