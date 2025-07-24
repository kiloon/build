package com.example.iotmanager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button scanButton = findViewById(R.id.scan_button);
        Button wifiButton = findViewById(R.id.wifi_button);
        TextView statusText = findViewById(R.id.status_text);
        
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Scanning for IoT devices...", Toast.LENGTH_SHORT).show();
                statusText.setText("Scanning for IoT devices...");
            }
        });
        
        wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "WiFi Settings - Feature coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
