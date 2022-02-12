package com.example.rewind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.rewind.audio.Boombox;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boombox.getInstance().load(this);
        setTheme(R.style.Theme_Rewind);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = this.findViewById(R.id.bottom_navigatin_view);
        NavHostFragment navFragment = (NavHostFragment) this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navFragment != null;
        NavigationUI.setupWithNavController(bottomNavigationView,navFragment.getNavController());
    }
}
