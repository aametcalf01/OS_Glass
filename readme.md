# Files and Directories
<ins>GlassDemoApp</ins>: directory containing the Glass demo app

<ins>JLGData</ins>: data collected from onsite visit to JLG

<ins>OS Internship Report</ins>: report of findings from summer intership with optimal solutions

<ins>WPA2_Enterprise.pdf</ins>: correspondence with google regarding connecting glass to WPA2 enterprise connection

<ins>classification</ins>: directory containing the classification app

<ins>droidAtScreen-1.2.jar</ins>: application to perform screen mirroring on mac with Google Glass

<ins>objectDetection</ins>: directory containing the object detection apps

<ins>timeTests.xlsx</ins>: classification and object detection infernce times

<ins>videoDemonstrations</ins>: Glass app demonstrations


# Working with Glass and Android Studio

Android Studio and ADB installation on Mac:
Install Android Studio for Mac (Use all the defaults)
	This should install the folder /Users/<username>/Library/Android

To enable the adb command, add the following to your .bash_profile:
	export PATH=${PATH}:/Users<username>f/Library/Android/sdk/platform-tools

Must also enable USB debugging from settings/device info on glass

Great website for adb commands:
     http://adbshell.com/commands