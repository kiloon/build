package com.example.myandroidapp;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    
    private static final int PERMISSIONS_REQUEST_CODE = 1001;
    
    private WifiManager wifiManager;
    private TextView statusText;
    private TextView deviceCountText;
    private Button scanButton;
    private Button wifiSettingsButton;
    private RecyclerView deviceRecyclerView;
    private IoTDeviceAdapter deviceAdapter;
    private List<IoTDevice> deviceList;
    
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
        setContentView(R.layout.activity_main);
        
        initializeViews();
        initializeWifi();
        setupRecyclerView();
        checkPermissions();
    }
    
    private void initializeViews() {
        statusText = findViewById(R.id.status_text);
        deviceCountText = findViewById(R.id.device_count_text);
        scanButton = findViewById(R.id.scan_button);
        wifiSettingsButton = findViewById(R.id.wifi_settings_button);
        deviceRecyclerView = findViewById(R.id.device_recycler_view);
        
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDeviceScan();
            }
        });
        
        wifiSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWifiSettings();
            }
        });
    }
    
    private void initializeWifi() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        
        if (!wifiManager.isWifiEnabled()) {
            statusText.setText("WiFi is disabled. Please enable WiFi to scan for IoT devices.");
        } else {
            statusText.setText("Ready to scan for IoT devices");
        }
    }
    
    private void setupRecyclerView() {
        deviceList = new ArrayList<>();
        deviceAdapter = new IoTDeviceAdapter(this, deviceList);
        deviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deviceRecyclerView.setAdapter(deviceAdapter);
        
        updateDeviceCount();
    }
    
    private void checkPermissions() {
        String[] permissions = {
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        };
        
        List<String> permissionsNeeded = new ArrayList<>();
        
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(permission);
            }
        }
        
        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, 
                permissionsNeeded.toArray(new String[0]), 
                PERMISSIONS_REQUEST_CODE);
        }
    }
    
    private void startDeviceScan() {
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "Please enable WiFi first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        statusText.setText("Scanning for IoT devices...");
        scanButton.setEnabled(false);
        
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
        
        boolean success = wifiManager.startScan();
        if (!success) {
            statusText.setText("Failed to start WiFi scan");
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
        deviceList.clear();
        
        for (ScanResult result : scanResults) {
            if (isIoTDevice(result.SSID)) {
                IoTDevice device = createIoTDevice(result);
                deviceList.add(device);
            }
        }
        
        deviceAdapter.notifyDataSetChanged();
        updateDeviceCount();
        statusText.setText("Scan completed");
        scanButton.setEnabled(true);
    }
    
    private boolean isIoTDevice(String ssid) {
        if (ssid == null || ssid.isEmpty()) return false;
        
        String lowerSSID = ssid.toLowerCase();
        return lowerSSID.contains("esp") || lowerSSID.contains("arduino") || 
               lowerSSID.contains("iot") || lowerSSID.contains("smart") ||
               lowerSSID.contains("cam") || lowerSSID.contains("light") ||
               lowerSSID.contains("sensor") || lowerSSID.contains("switch") ||
               lowerSSID.contains("pi") || lowerSSID.contains("raspberry");
    }
    
    private IoTDevice createIoTDevice(ScanResult result) {
        IoTDevice device = new IoTDevice();
        device.setName(result.SSID);
        device.setMacAddress(result.BSSID);
        device.setSignalStrength(result.level);
        device.setType(determineDeviceType(result.SSID));
        device.setConnected(false);
        device.setLastSeen(System.currentTimeMillis());
        
        return device;
    }
    
    private String determineDeviceType(String ssid) {
        if (ssid == null) return "Unknown";
        
        String lowerSSID = ssid.toLowerCase();
        if (lowerSSID.contains("cam") || lowerSSID.contains("camera")) return "Camera";
        if (lowerSSID.contains("light") || lowerSSID.contains("bulb")) return "Light";
        if (lowerSSID.contains("switch") || lowerSSID.contains("relay")) return "Switch";
        if (lowerSSID.contains("sensor") || lowerSSID.contains("temp")) return "Sensor";
        if (lowerSSID.contains("esp")) return "ESP32/ESP8266";
        if (lowerSSID.contains("arduino")) return "Arduino";
        if (lowerSSID.contains("pi") || lowerSSID.contains("raspberry")) return "Raspberry Pi";
        
        return "IoT Device";
    }
    
    private void updateDeviceCount() {
        deviceCountText.setText("Devices found: " + deviceList.size());
    }
    
    private void openWifiSettings() {
        Intent intent = new Intent(this, WifiSettingsActivity.class);
        startActivity(intent);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            
            if (allGranted) {
                Toast.makeText(this, "Permissions granted. Ready to scan!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions required for WiFi scanning", Toast.LENGTH_LONG).show();
            }
        }
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