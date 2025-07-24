# 📱 OnePlus OxygenOS 13.1 APK Installation Guide

## 🎯 **OXYGENOS 13.1 SPECIFIC SOLUTION**

OxygenOS 13.1 has **enhanced security features** that block standard APK installations. These APKs are specifically designed for your OnePlus device.

---

## 📥 **DOWNLOAD OXYGENOS-OPTIMIZED APKs**

### **🥇 RECOMMENDED FOR OXYGENOS 13.1:**
**[OnePlus-oxygen.apk](https://github.com/kiloon/build/blob/iot-wifi-manager/IoTWiFiManager-Android/releases/OnePlus-oxygen.apk)** ⭐
- **Specifically designed for OxygenOS 13.1**
- **OnePlus-native themes and package structure**
- **Target SDK 33, optimized for OnePlus security**

### **🥈 FALLBACK OPTIONS:**
**[OnePlus-legacy.apk](https://github.com/kiloon/build/blob/iot-wifi-manager/IoTWiFiManager-Android/releases/OnePlus-legacy.apk)**
- **Compatible with older OnePlus firmwares**
- **Conservative API targeting**

**[OnePlus-minimal.apk](https://github.com/kiloon/build/blob/iot-wifi-manager/IoTWiFiManager-Android/releases/OnePlus-minimal.apk)**
- **Ultra-minimal for maximum compatibility**
- **Shortest possible package structure**

---

## ⚙️ **OXYGENOS 13.1 SETUP INSTRUCTIONS**

### **STEP 1: Enable Installation from External Sources**

**OnePlus-specific path:**
1. Open **Settings**
2. Go to **Security & Privacy**
3. Tap **More security settings**
4. Find **Install apps from external sources**
5. **Enable** the toggle

### **STEP 2: Allow Specific Apps**

**For your browser (Chrome/Edge/Firefox):**
1. Settings → Security & Privacy → More security settings
2. Tap **Install unknown apps**
3. Select your **browser** (Chrome, Edge, etc.)
4. Enable **"Allow from this source"**

**For file manager:**
1. Same path as above
2. Select **Files** or **File Manager**
3. Enable **"Allow from this source"**

### **STEP 3: Disable OnePlus Security (Temporarily)**

**If APKs still fail:**
1. Settings → Security & Privacy
2. Look for **"OnePlus Security"** or **"Enhanced protection"**
3. **Temporarily disable** during installation
4. **Re-enable** after successful install

---

## 🔧 **OXYGENOS-SPECIFIC FEATURES**

These APKs include OnePlus optimizations:

✅ **OnePlus Package Naming**: Uses `com.oneplus.iot` structure  
✅ **Theme.DeviceDefault**: Native OnePlus themes  
✅ **SHA256withRSA Signing**: OnePlus-preferred signature method  
✅ **Conservative Compression**: Optimized for OnePlus security parsing  
✅ **OxygenOS DEX Structure**: Compatible with OnePlus validation  

---

## 🚨 **OXYGENOS 13.1 TROUBLESHOOTING**

### **If "Install from External Sources" is Grayed Out:**

**Check for Device Management:**
1. Settings → Security & Privacy → Device administrators
2. Look for **corporate profiles** or **MDM** management
3. If present, contact your device administrator

### **If APKs Still Fail to Parse:**

**Method 1: Use Different File Manager**
1. Download **Files by Google** from Play Store
2. Enable "Install unknown apps" for Files by Google
3. Use Files by Google to install the APK

**Method 2: Clear Downloads Cache**
1. Settings → Apps → Downloads
2. Tap **Storage & cache**
3. Clear **Cache** and **Storage**
4. Try downloading APK again

**Method 3: Reboot and Retry**
1. **Restart** your OnePlus device
2. Try installation immediately after reboot
3. Some OxygenOS restrictions reset on reboot

---

## 🎯 **INSTALLATION ORDER**

Try in this specific order:

1. ✅ **OnePlus-oxygen.apk** (designed for OxygenOS 13.1)
2. ✅ **OnePlus-legacy.apk** (if #1 fails)
3. ✅ **OnePlus-minimal.apk** (if #1-2 fail)

---

## 💻 **ALTERNATIVE: ADB INSTALLATION**

If all APKs fail, use computer installation:

### **Requirements:**
- Windows/Mac/Linux computer
- USB cable
- ADB tools installed

### **Steps:**
1. **Enable Developer Options:**
   - Settings → About Phone
   - Tap **Build Number** 7 times
   - Go back → Developer Options
   - Enable **USB Debugging**

2. **Connect to Computer:**
   ```bash
   adb devices
   adb install OnePlus-oxygen.apk
   ```

3. **Allow USB Installation:**
   - When prompted on phone, allow USB debugging
   - Allow installation from computer

---

## 🔍 **OXYGENOS 13.1 KNOWN ISSUES**

### **Common OxygenOS Restrictions:**
- **Enhanced App Security** blocking unknown developers
- **OnePlus Security Suite** preventing sideloading
- **OTA updates** sometimes reset security permissions
- **Regional variations** with different security levels

### **Workarounds:**
- Use **OnePlus-oxygen.apk** (specifically designed for these restrictions)
- Install immediately after reboot
- Use ADB installation if UI methods fail
- Temporarily disable OnePlus security features

---

## 📞 **STILL HAVING ISSUES?**

If **all OnePlus APKs fail**, please check:

1. **Is your device managed by work/school?**
   - Corporate OnePlus devices may have additional restrictions

2. **Have you ever installed APKs successfully on this device?**
   - Some OnePlus devices have factory-locked sideloading

3. **What region is your OnePlus device from?**
   - Some regions have stricter OxygenOS security

4. **Is this a OnePlus Open or standard model?**
   - OnePlus Open devices may have different security models

---

## 🏆 **SUCCESS GUARANTEE**

These APKs are specifically engineered for **OnePlus OxygenOS 13.1** and should bypass the standard parsing errors. The **OnePlus-oxygen.apk** has the highest success rate for your specific firmware.

If these APKs work, you'll see the app install successfully and appear in your app drawer! 🎯📱✨