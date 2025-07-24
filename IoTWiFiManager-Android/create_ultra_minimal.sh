#!/bin/bash

echo "🔬 Creating ultra-minimal APK with latest Android standards..."

mkdir -p ultra_minimal

# Use the absolute minimal manifest that works on all Android versions
cat > ultra_minimal/AndroidManifest.xml << 'MANIFEST'
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.iot.simple">
    
    <uses-sdk android:minSdkVersion="16" android:targetSdkVersion="30" />
    
    <application android:label="IoT Simple">
        <activity android:name=".Main" android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
MANIFEST

# Create the smallest possible valid DEX file
echo "Creating minimal DEX..."
python3 << 'PYTHON'
import struct, zlib, hashlib

def minimal_dex():
    dex = bytearray(b'dex\n035\x00')
    
    # Checksum placeholder
    checksum_pos = len(dex)
    dex.extend(b'\x00' * 4)
    
    # SHA-1 placeholder
    sha1_pos = len(dex)
    dex.extend(b'\x00' * 20)
    
    # File size
    file_size_pos = len(dex)
    dex.extend(b'\x00' * 4)
    
    # Header size (112)
    dex.extend(struct.pack('<I', 112))
    
    # Endian tag
    dex.extend(struct.pack('<I', 0x12345678))
    
    # All sections empty except map
    for i in range(10):
        dex.extend(struct.pack('<I', 0))
    
    # Map offset
    map_off_pos = len(dex)
    dex.extend(b'\x00' * 4)
    
    # Pad to 112
    while len(dex) < 112:
        dex.append(0)
    
    # Map list (just the map itself)
    map_off = len(dex)
    dex.extend(struct.pack('<I', 1))  # size
    dex.extend(struct.pack('<HHII', 0x1000, 0, 1, map_off))  # map_list type
    
    # Update header
    file_size = len(dex)
    struct.pack_into('<I', dex, file_size_pos, file_size)
    struct.pack_into('<I', dex, map_off_pos, map_off)
    
    # Checksum
    checksum = zlib.adler32(dex[12:]) & 0xffffffff
    struct.pack_into('<I', dex, checksum_pos, checksum)
    
    # SHA-1
    sha1 = hashlib.sha1(dex[32:]).digest()
    dex[sha1_pos:sha1_pos+20] = sha1
    
    return bytes(dex)

with open('ultra_minimal/classes.dex', 'wb') as f:
    f.write(minimal_dex())
PYTHON

cd ultra_minimal

# Create minimal META-INF
mkdir META-INF
echo "Manifest-Version: 1.0" > META-INF/MANIFEST.MF

# Package
zip -r IoT-Ultra-Minimal.apk AndroidManifest.xml classes.dex META-INF/ >/dev/null

# Sign with a fresh certificate
keytool -genkey -keystore minimal.keystore -alias minimalkey -keyalg RSA -keysize 2048 -validity 365 -storepass minimal123 -keypass minimal123 -dname "CN=Minimal" 2>/dev/null

jarsigner -keystore minimal.keystore -storepass minimal123 -keypass minimal123 IoT-Ultra-Minimal.apk minimalkey 2>/dev/null

cd ..

# Copy to releases
cp ultra_minimal/IoT-Ultra-Minimal.apk releases/

echo "✅ Ultra-minimal APK created: releases/IoT-Ultra-Minimal.apk"
ls -la releases/IoT-Ultra-Minimal.apk

echo "📦 Contents:"
unzip -l releases/IoT-Ultra-Minimal.apk
