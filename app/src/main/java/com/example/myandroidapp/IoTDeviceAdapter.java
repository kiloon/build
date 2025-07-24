package com.example.myandroidapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class IoTDeviceAdapter extends RecyclerView.Adapter<IoTDeviceAdapter.DeviceViewHolder> {
    
    private Context context;
    private List<IoTDevice> deviceList;
    
    public IoTDeviceAdapter(Context context, List<IoTDevice> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }
    
    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_iot_device, parent, false);
        return new DeviceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        IoTDevice device = deviceList.get(position);
        
        holder.deviceName.setText(device.getName());
        holder.deviceType.setText(device.getType());
        holder.deviceMac.setText(device.getMacAddress());
        holder.signalText.setText(device.getSignalStrengthText());
        holder.signalProgress.setProgress(device.getSignalStrengthPercentage());
        
        // Set connection status
        if (device.isConnected()) {
            holder.connectionStatus.setText("Connected");
            holder.connectionStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.connectionStatus.setText("Disconnected");
            holder.connectionStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        
        // Set device icon based on type
        setDeviceIcon(holder.deviceIcon, device.getType());
        
        // Set click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IoTDeviceActivity.class);
                intent.putExtra("device_name", device.getName());
                intent.putExtra("device_type", device.getType());
                intent.putExtra("device_mac", device.getMacAddress());
                intent.putExtra("device_signal", device.getSignalStrength());
                context.startActivity(intent);
            }
        });
    }
    
    private void setDeviceIcon(ImageView iconView, String deviceType) {
        // Set appropriate icon based on device type
        // For now, using system icons - in a real app you'd use custom icons
        if (deviceType.contains("Camera")) {
            iconView.setImageResource(android.R.drawable.ic_menu_camera);
        } else if (deviceType.contains("Light")) {
            iconView.setImageResource(android.R.drawable.ic_menu_day);
        } else if (deviceType.contains("Switch")) {
            iconView.setImageResource(android.R.drawable.ic_menu_preferences);
        } else if (deviceType.contains("Sensor")) {
            iconView.setImageResource(android.R.drawable.ic_menu_info_details);
        } else {
            iconView.setImageResource(android.R.drawable.ic_menu_compass);
        }
    }
    
    @Override
    public int getItemCount() {
        return deviceList.size();
    }
    
    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName, deviceType, deviceMac, signalText, connectionStatus;
        ProgressBar signalProgress;
        ImageView deviceIcon;
        
        public DeviceViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.device_name);
            deviceType = itemView.findViewById(R.id.device_type);
            deviceMac = itemView.findViewById(R.id.device_mac);
            signalText = itemView.findViewById(R.id.signal_text);
            signalProgress = itemView.findViewById(R.id.signal_progress);
            connectionStatus = itemView.findViewById(R.id.connection_status);
            deviceIcon = itemView.findViewById(R.id.device_icon);
        }
    }
}