package com.example.rewind;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rewind.audio.Boombox;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boombox.getInstance().load(this);
        setContentView(R.layout.activity_main);
    }
}
