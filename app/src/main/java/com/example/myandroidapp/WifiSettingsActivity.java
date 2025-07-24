package com.example.myandroidapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class WifiSettingsActivity extends Activity {
    
    private WifiManager wifiManager;
    private Switch wifiSwitch;
    private Button scanButton;
    private TextView statusText;
    private RecyclerView networkRecyclerView;
    private WifiNetworkAdapter networkAdapter;
    private List<ScanResult> networkList;
    
    private BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                scanSuccess();
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_settings);
        
        initializeViews();
        initializeWifi();
        setupRecyclerView();
        updateWifiStatus();
    }
    
    private void initializeViews() {
        wifiSwitch = findViewById(R.id.wifi_switch);
        scanButton = findViewById(R.id.scan_networks_button);
        statusText = findViewById(R.id.wifi_status_text);
        networkRecyclerView = findViewById(R.id.network_recycler_view);
        
        wifiSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleWifi(isChecked);
        });
        
        scanButton.setOnClickListener(v -> startNetworkScan());
    }
    
    private void initializeWifi() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }
    
    private void setupRecyclerView() {
        networkList = new ArrayList<>();
        networkAdapter = new WifiNetworkAdapter(this, networkList);
        networkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        networkRecyclerView.setAdapter(networkAdapter);
    }
    
    private void updateWifiStatus() {
        boolean isWifiEnabled = wifiManager.isWifiEnabled();
        wifiSwitch.setChecked(isWifiEnabled);
        
        if (isWifiEnabled) {
            statusText.setText("WiFi is enabled");
            scanButton.setEnabled(true);
        } else {
            statusText.setText("WiFi is disabled");
            scanButton.setEnabled(false);
            networkList.clear();
            networkAdapter.notifyDataSetChanged();
        }
    }
    
    private void toggleWifi(boolean enable) {
        if (enable) {
            wifiManager.setWifiEnabled(true);
            statusText.setText("Enabling WiFi...");
        } else {
            wifiManager.setWifiEnabled(false);
            statusText.setText("Disabling WiFi...");
            networkList.clear();
            networkAdapter.notifyDataSetChanged();
        }
        
        // Update UI after a delay to reflect actual WiFi state
        statusText.postDelayed(() -> updateWifiStatus(), 2000);
    }
    
    private void startNetworkScan() {
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "Please enable WiFi first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        statusText.setText("Scanning for networks...");
        scanButton.setEnabled(false);
        
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
        
        boolean success = wifiManager.startScan();
        if (!success) {
            statusText.setText("Failed to start network scan");
            scanButton.setEnabled(true);
        }
    }
    
    private void scanSuccess() {
        try {
            unregisterReceiver(wifiScanReceiver);
        } catch (Exception e) {
            // Receiver already unregistered
        }
        
        List<ScanResult> scanResults = wifiManager.getScanResults();
        networkList.clear();
        networkList.addAll(scanResults);
        
        networkAdapter.notifyDataSetChanged();
        statusText.setText("Found " + networkList.size() + " networks");
        scanButton.setEnabled(true);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(wifiScanReceiver);
        } catch (Exception e) {
            // Receiver already unregistered
        }
    }
}