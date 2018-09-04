# Files and Directories
GlassDemoApp: directory containing the Glass demo app
JLGData: data collected from onsite visit to JLG
OS Internship Report: report of findings from summer intership with optimal solutions
WPA2_Enterprise.pdf: correspondence with google regarding connecting glass to WPA2 enterprise connection
classification: directory containing the classification app
droidAtScreen-1.2.jar: application to perform screen mirroring on mac with Google Glass
objectDetection: directory containing the object detection apps
timeTests.xlsx: classification and object detection infernce times
videoDemonstrations: Glass app demonstrations


# Working with Glass and Android Studio

Android Studio and ADB installation on Mac:
Install Android Studio for Mac (Use all the defaults)
	This should install the folder /Users/<username>/Library/Android

To enable the adb command, add the following to your .bash_profile:
	export PATH=${PATH}:/Users<username>f/Library/Android/sdk/platform-tools

Must also enable USB debugging from settings/device info on glass

Great website for adb commands:
     http://adbshell.com/commands