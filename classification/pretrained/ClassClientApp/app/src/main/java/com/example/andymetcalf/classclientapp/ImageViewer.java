package com.example.andymetcalf.classclientapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.FileObserver;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.glass.content.Intents;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ImageViewer extends Activity
    implements GestureDetector.BaseListener, GestureDetector.FingerListener {

    private ImageView mImageView;
    private TextView mheader;
    private GestureDetector mGestureDetector;

    long startTime;
    long duration;


    int portNum = 8000;
    int portNum2 = 8001;

    //NEED TO UPDATE THIS
    String ip = "192.168.1.207";  //STILL NEED TO GET THIS MANUALLY ***TODO: MAKE AUTOMATIC

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.text_layout);
        mheader = (TextView)findViewById(R.id.header);
        mheader.setText("Tap to take picture");



        // Initialize the gesture detector and set the activity to listen to discrete gestures.
        mGestureDetector = new GestureDetector(this).setBaseListener(this).setFingerListener(this);
    }

    /**
     * Overridden to allow the gesture detector to process motion events that occur anywhere within
     * the activity.
     */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return mGestureDetector.onMotionEvent(event);
    }

    @Override
    public boolean onGesture(Gesture gesture){
        if (gesture == Gesture.TAP){
            startTime = System.nanoTime();
            takePicture();
            return true;
        }else if(gesture == Gesture.SWIPE_DOWN){
            startActivity(new Intent(ImageViewer.this, MainActivity.class));
            return true;
        }
        else{
            return true; //TODO: need to make sure down swipe still works!!
        }
    }

    @Override
    public void onFingerCountChanged(int i, int i1) {

    }

    /*** boiler plate google code
     * https://developers.google.com/glass/develop/gdk/media-camera/camera
     */
    private static final int TAKE_PICTURE_REQUEST = 1;

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
            String picturePath = data.getStringExtra(
                    Intents.EXTRA_PICTURE_FILE_PATH);
            processPictureWhenReady(picturePath);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("SetTextI18n")
    private void processPictureWhenReady(final String picturePath) {
        final File pictureFile = new File(picturePath);
        if(pictureFile.exists()){
            //The picture is ready; process it

            //Create byte array to store picture that was just taken
            byte [] mybytearray = new byte[(int)pictureFile.length()];

            //This function is a hack to avoid Network on Main Error
            enableStrictMode();
            try(
                    Socket socket = new Socket(ip,portNum);
                    FileInputStream fis = new FileInputStream(pictureFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    OutputStream os = socket.getOutputStream();
            ){
                //write logic here

                bis.read(mybytearray,0,mybytearray.length);
                os.write(mybytearray,0,mybytearray.length);
                os.close();
            }catch (UnknownHostException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }


            int filesize = 6022386;
            int bytesRead;
            int current =0;

            byte [] mybytearray2 = new byte[filesize];
            String picturePath2 = "/storage/emulated/legacy/DCIM/Camera/test.jpg";

            while (true) {

                try {
                    enableStrictMode();
                    Socket socket2 = new Socket(ip, portNum2);
                    InputStream is = socket2.getInputStream();
                    FileOutputStream fos = new FileOutputStream(picturePath2);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    bytesRead = is.read(mybytearray2,0,mybytearray2.length);
                    Log.d("BYTE","There are this many: "+Integer.toString(bytesRead));
                    current = bytesRead;

                    do{
                        bytesRead=
                                is.read(mybytearray2,current,(mybytearray2.length-current));
                        if(bytesRead>-1)current+=bytesRead;
                    }while (bytesRead>-1);

                    bos.write(mybytearray2,0,current);
                    socket2.close();
                    setContentView(R.layout.image_layout);
                    mImageView = (ImageView) findViewById(R.id.mainImageView);
                    mImageView.setImageURI(Uri.parse(picturePath2));
                    break;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                duration = System.nanoTime()-startTime;
                duration = duration/1000000000;

                Log.d("TIME", Long.toString(duration)+" seconds");

//                setContentView(R.layout.image_layout);
//                mImageView = (ImageView) findViewById(R.id.mainImageView);
//                mImageView.setImageURI(Uri.parse(picturePath2));

            }


        }else {
            // The file does not exist yet. Before starting the file observer, you
            // can update your UI to let the user know that the application is
            // waiting for the picture (for example, by displaying the thumbnail
            // image and a progress indicator).

            final File parentDirectory = pictureFile.getParentFile();
            FileObserver observer = new FileObserver(parentDirectory.getPath()) {
                // Protect against additional pending events after CLOSE_WRITE is
                // handled.
                private boolean isFileWritten;

                @Override
                public void onEvent(int event, String path) {
                    if (!isFileWritten) {
                        // For safety, make sure that the file that was created in
                        // the directory is actually the one that we're expecting.
                        File affectedFile = new File(parentDirectory, path);
                        isFileWritten = (event == FileObserver.CLOSE_WRITE
                                && affectedFile.equals(pictureFile));

                        if (isFileWritten) {
                            stopWatching();

                            // Now that the file is ready, recursively call
                            // processPictureWhenReady again (on the UI thread).
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    processPictureWhenReady(picturePath);
                                }
                            });
                        }
                    }
                }
            };
            observer.startWatching();
        }
    }
    /**
     * end of boiler plate
     */

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
}
