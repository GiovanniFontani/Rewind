
package com.example.rewind;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class WiFiDetectorActivity extends AppCompatActivity {
    private EditText ipConfigEditText;
    private Button connectionButton;
    private Button closeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_detector);
        ipConfigEditText = findViewById(R.id.ipconfig_edit_text);
        connectionButton = findViewById(R.id.connect_button);
        closeButton = findViewById(R.id.close_button);

        ipConfigEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(!ipConfigEditText.getText().toString().isEmpty()) {
                    connectionButton.setEnabled(true);
                }else
                    connectionButton.setEnabled(false);
                return false;
            }
        });

        connectionButton.setOnClickListener(view -> {
                Intent replyIntent = new Intent();
                setResult(RESULT_OK, replyIntent);
                replyIntent.putExtra("ipaddress", ipConfigEditText.getText().toString());
                finish();
        });

        closeButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            setResult(RESULT_CANCELED, replyIntent);
            finish();
        });
    }
}