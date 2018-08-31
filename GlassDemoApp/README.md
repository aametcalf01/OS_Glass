# GlassDemo

This POC app demonstrates a simple timer, camera, voice features and a barcode reader.

## Instructions for sideloading app with Android Debug Bridge (adb)

1. Install adb on local machine [link] https://www.xda-developers.com/install-adb-windows-macos-linux/
2. Turn on USB debugging --> settings > device info > turn on debug
3. Connect Glass via USB
4. Type "adb devices" in the command line shell to determine if the Glass is recognized
5. Type "adb install -t \<path\>/GlassDemo/app/build/outputs/apk/debug/app-debug.apk" (replacing \"\<path\>\" with the path to GlassDemo)

## Instructions for sideloading app with Android Studio

1. Download Android Studio and build app from within the IDE 
      * (May have to remove the \"build\" directory after cloning the repo)

