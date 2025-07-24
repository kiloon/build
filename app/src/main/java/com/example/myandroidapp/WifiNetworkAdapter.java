package com.example.myandroidapp;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WifiNetworkAdapter extends RecyclerView.Adapter<WifiNetworkAdapter.NetworkViewHolder> {
    
    private Context context;
    private List<ScanResult> networkList;
    
    public WifiNetworkAdapter(Context context, List<ScanResult> networkList) {
        this.context = context;
        this.networkList = networkList;
    }
    
    @Override
    public NetworkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wifi_network, parent, false);
        return new NetworkViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(NetworkViewHolder holder, int position) {
        ScanResult network = networkList.get(position);
        
        holder.networkName.setText(network.SSID.isEmpty() ? "Hidden Network" : network.SSID);
        holder.networkSecurity.setText(getSecurityType(network));
        holder.signalText.setText(getSignalStrengthText(network.level));
        holder.signalProgress.setProgress(getSignalStrengthPercentage(network.level));
        
        // Set security icon
        setSecurityIcon(holder.securityIcon, network);
        
        // Set signal icon
        setSignalIcon(holder.signalIcon, network.level);
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> connectToNetwork(network));
    }
    
    private String getSecurityType(ScanResult network) {
        String capabilities = network.capabilities;
        if (capabilities.contains("WPA3")) return "WPA3";
        if (capabilities.contains("WPA2")) return "WPA2";
        if (capabilities.contains("WPA")) return "WPA";
        if (capabilities.contains("WEP")) return "WEP";
        return "Open";
    }
    
    private String getSignalStrengthText(int signalStrength) {
        if (signalStrength >= -50) return "Excellent";
        if (signalStrength >= -60) return "Good";
        if (signalStrength >= -70) return "Fair";
        return "Poor";
    }
    
    private int getSignalStrengthPercentage(int signalStrength) {
        if (signalStrength >= -50) return 100;
        if (signalStrength >= -60) return 75;
        if (signalStrength >= -70) return 50;
        if (signalStrength >= -80) return 25;
        return 10;
    }
    
    private void setSecurityIcon(ImageView iconView, ScanResult network) {
        String security = getSecurityType(network);
        if ("Open".equals(security)) {
            iconView.setImageResource(android.R.drawable.ic_lock_idle_low_battery);
        } else {
            iconView.setImageResource(android.R.drawable.ic_lock_lock);
        }
    }
    
    private void setSignalIcon(ImageView iconView, int signalStrength) {
        if (signalStrength >= -60) {
            iconView.setImageResource(android.R.drawable.ic_menu_compass);
        } else if (signalStrength >= -70) {
            iconView.setImageResource(android.R.drawable.ic_menu_mylocation);
        } else {
            iconView.setImageResource(android.R.drawable.ic_menu_search);
        }
    }
    
    private void connectToNetwork(ScanResult network) {
        String networkName = network.SSID.isEmpty() ? "Hidden Network" : network.SSID;
        String securityType = getSecurityType(network);
        
        if ("Open".equals(securityType)) {
            Toast.makeText(context, "Connecting to " + networkName + " (Open Network)", Toast.LENGTH_SHORT).show();
            // In a real app, you would implement actual connection logic here
        } else {
            Toast.makeText(context, "Would show password dialog for " + networkName + " (" + securityType + ")", Toast.LENGTH_LONG).show();
            // In a real app, you would show a password input dialog here
        }
    }
    
    @Override
    public int getItemCount() {
        return networkList.size();
    }
    
    public static class NetworkViewHolder extends RecyclerView.ViewHolder {
        TextView networkName, networkSecurity, signalText;
        ProgressBar signalProgress;
        ImageView securityIcon, signalIcon;
        
        public NetworkViewHolder(View itemView) {
            super(itemView);
            networkName = itemView.findViewById(R.id.network_name);
            networkSecurity = itemView.findViewById(R.id.network_security);
            signalText = itemView.findViewById(R.id.signal_text);
            signalProgress = itemView.findViewById(R.id.signal_progress);
            securityIcon = itemView.findViewById(R.id.security_icon);
            signalIcon = itemView.findViewById(R.id.signal_icon);
        }
    }
}