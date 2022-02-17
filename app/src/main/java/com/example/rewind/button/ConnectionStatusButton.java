package com.example.rewind.button;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.rewind.R;
import com.example.rewind.VideoPlayerFragment;

public class ConnectionStatusButton {

    private CardView cardView;
    private ProgressBar progressBar;
    private TextView textView;
    private ConstraintLayout constraintLayout;
    private boolean connected = false;

    Animation fade_in;

    public ConnectionStatusButton(Context ct, View view){
        cardView = view.findViewById(R.id.carView_connecting);
        constraintLayout = view.findViewById(R.id.connecting_layout);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.connecting_text_view);
    }

    public void buttonDisconnect(){
            constraintLayout.setBackgroundColor(cardView.getResources().getColor(R.color.red_connect_button));
            textView.setText(R.string.video_connect_button);
            connected = false;
    }

    public void buttonConnecting(){
            progressBar.setVisibility(View.VISIBLE);
            constraintLayout.setBackgroundColor(cardView.getResources().getColor(R.color.orange_connecting_unpressed));
            textView.setText(R.string.video_connecting_button);

    }

    public void buttonConnected(){
            constraintLayout.setBackgroundColor(cardView.getResources().getColor(R.color.green_connected_button));
            progressBar.setVisibility(View.GONE);
            textView.setText(R.string.video_connected_button);
            connected = true;
    }

    public boolean getConnectionStatus(){
        return connected;
    }

    public void setConnectionStatus(boolean connected){
        this.connected = connected;
    }

}
