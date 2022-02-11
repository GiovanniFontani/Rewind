package com.example.rewind;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.rewind.audio.Boombox;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boombox.getInstance().load(this);
        setTheme(R.style.Theme_Rewind);
        setContentView(R.layout.activity_main);
    }
}
