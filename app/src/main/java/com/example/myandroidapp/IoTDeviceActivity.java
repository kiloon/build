package com.example.myandroidapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IoTDeviceActivity extends Activity {
    
    private TextView deviceNameText, deviceTypeText, deviceMacText, deviceSignalText;
    private TextView connectionStatusText, responseText;
    private Button connectButton, sendCommandButton, refreshButton;
    private EditText commandInput;
    
    private String deviceName, deviceType, deviceMac;
    private int deviceSignal;
    private boolean isConnected = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot_device);
        
        initializeViews();
        loadDeviceInfo();
        setupClickListeners();
    }
    
    private void initializeViews() {
        deviceNameText = findViewById(R.id.device_name_text);
        deviceTypeText = findViewById(R.id.device_type_text);
        deviceMacText = findViewById(R.id.device_mac_text);
        deviceSignalText = findViewById(R.id.device_signal_text);
        connectionStatusText = findViewById(R.id.connection_status_text);
        responseText = findViewById(R.id.response_text);
        
        connectButton = findViewById(R.id.connect_button);
        sendCommandButton = findViewById(R.id.send_command_button);
        refreshButton = findViewById(R.id.refresh_button);
        commandInput = findViewById(R.id.command_input);
    }
    
    private void loadDeviceInfo() {
        deviceName = getIntent().getStringExtra("device_name");
        deviceType = getIntent().getStringExtra("device_type");
        deviceMac = getIntent().getStringExtra("device_mac");
        deviceSignal = getIntent().getIntExtra("device_signal", -100);
        
        deviceNameText.setText(deviceName);
        deviceTypeText.setText(deviceType);
        deviceMacText.setText(deviceMac);
        deviceSignalText.setText(getSignalStrengthText(deviceSignal) + " (" + deviceSignal + " dBm)");
        
        updateConnectionStatus();
    }
    
    private void setupClickListeners() {
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleConnection();
            }
        });
        
        sendCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand();
            }
        });
        
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDeviceStatus();
            }
        });
    }
    
    private void toggleConnection() {
        if (isConnected) {
            disconnectFromDevice();
        } else {
            connectToDevice();
        }
    }
    
    private void connectToDevice() {
        connectButton.setEnabled(false);
        responseText.setText("Connecting to device...");
        
        // Simulate connection attempt
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Thread.sleep(2000); // Simulate connection delay
                    return Math.random() > 0.2; // 80% success rate
                } catch (InterruptedException e) {
                    return false;
                }
            }
            
            @Override
            protected void onPostExecute(Boolean success) {
                connectButton.setEnabled(true);
                if (success) {
                    isConnected = true;
                    updateConnectionStatus();
                    responseText.setText("Successfully connected to " + deviceName);
                    Toast.makeText(IoTDeviceActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
                } else {
                    responseText.setText("Failed to connect to device. Please try again.");
                    Toast.makeText(IoTDeviceActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
    
    private void disconnectFromDevice() {
        isConnected = false;
        updateConnectionStatus();
        responseText.setText("Disconnected from " + deviceName);
        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
    }
    
    private void sendCommand() {
        if (!isConnected) {
            Toast.makeText(this, "Please connect to device first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String command = commandInput.getText().toString().trim();
        if (command.isEmpty()) {
            Toast.makeText(this, "Please enter a command", Toast.LENGTH_SHORT).show();
            return;
        }
        
        sendCommandButton.setEnabled(false);
        responseText.setText("Sending command: " + command);
        
        // Simulate command execution
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    Thread.sleep(1500); // Simulate command execution delay
                    return generateMockResponse(params[0]);
                } catch (InterruptedException e) {
                    return "Error: Command execution interrupted";
                }
            }
            
            @Override
            protected void onPostExecute(String response) {
                sendCommandButton.setEnabled(true);
                responseText.setText("Response: " + response);
                commandInput.setText(""); // Clear input
            }
        }.execute(command);
    }
    
    private String generateMockResponse(String command) {
        String lowerCommand = command.toLowerCase();
        
        if (lowerCommand.contains("status")) {
            return "Device Status: Online, Temperature: 24°C, Uptime: 72h";
        } else if (lowerCommand.contains("on") || lowerCommand.contains("enable")) {
            return "Device turned ON successfully";
        } else if (lowerCommand.contains("off") || lowerCommand.contains("disable")) {
            return "Device turned OFF successfully";
        } else if (lowerCommand.contains("reboot") || lowerCommand.contains("restart")) {
            return "Device rebooting... Will be back online in 30 seconds";
        } else if (lowerCommand.contains("brightness")) {
            return "Brightness set to " + extractNumber(command) + "%";
        } else if (lowerCommand.contains("temperature")) {
            return "Current temperature: 24.5°C";
        } else {
            return "Command executed successfully. Response: OK";
        }
    }
    
    private String extractNumber(String text) {
        String numbers = text.replaceAll("[^0-9]", "");
        return numbers.isEmpty() ? "50" : numbers;
    }
    
    private void refreshDeviceStatus() {
        refreshButton.setEnabled(false);
        responseText.setText("Refreshing device status...");
        
        // Simulate status refresh
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                    return "Device refreshed. Status: " + (isConnected ? "Connected" : "Disconnected") + 
                           ", Signal: " + deviceSignal + " dBm, Last seen: Just now";
                } catch (InterruptedException e) {
                    return "Error refreshing status";
                }
            }
            
            @Override
            protected void onPostExecute(String status) {
                refreshButton.setEnabled(true);
                responseText.setText(status);
            }
        }.execute();
    }
    
    private void updateConnectionStatus() {
        if (isConnected) {
            connectionStatusText.setText("Status: Connected");
            connectionStatusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            connectButton.setText("Disconnect");
            sendCommandButton.setEnabled(true);
        } else {
            connectionStatusText.setText("Status: Disconnected");
            connectionStatusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            connectButton.setText("Connect");
            sendCommandButton.setEnabled(false);
        }
    }
    
    private String getSignalStrengthText(int signalStrength) {
        if (signalStrength >= -50) return "Excellent";
        if (signalStrength >= -60) return "Good";
        if (signalStrength >= -70) return "Fair";
        return "Poor";
    }
}