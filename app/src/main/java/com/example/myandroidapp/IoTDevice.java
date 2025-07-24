package com.example.myandroidapp;

public class IoTDevice {
    private String name;
    private String macAddress;
    private int signalStrength;
    private String type;
    private boolean connected;
    private String ipAddress;
    private long lastSeen;
    
    public IoTDevice() {
        this.connected = false;
        this.lastSeen = System.currentTimeMillis();
    }
    
    // Getters
    public String getName() { return name; }
    public String getMacAddress() { return macAddress; }
    public int getSignalStrength() { return signalStrength; }
    public String getType() { return type; }
    public boolean isConnected() { return connected; }
    public String getIpAddress() { return ipAddress; }
    public long getLastSeen() { return lastSeen; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    public void setSignalStrength(int signalStrength) { this.signalStrength = signalStrength; }
    public void setType(String type) { this.type = type; }
    public void setConnected(boolean connected) { this.connected = connected; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
    
    // Helper methods
    public String getSignalStrengthText() {
        if (signalStrength >= -50) return "Excellent";
        if (signalStrength >= -60) return "Good";
        if (signalStrength >= -70) return "Fair";
        return "Poor";
    }
    
    public int getSignalStrengthPercentage() {
        // Convert dBm to percentage (rough approximation)
        if (signalStrength >= -50) return 100;
        if (signalStrength >= -60) return 75;
        if (signalStrength >= -70) return 50;
        if (signalStrength >= -80) return 25;
        return 10;
    }
}