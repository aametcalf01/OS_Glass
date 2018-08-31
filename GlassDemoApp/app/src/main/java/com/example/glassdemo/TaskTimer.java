package com.example.glassdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.os.Handler;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.floor;


public class TaskTimer extends Activity
        implements GestureDetector.BaseListener, GestureDetector.FingerListener{

    private TextView mTimerHeader;
    private TextView mTimerActions;
    private TextView mTimerDisplay;
    private GestureDetector mGestureDetector;
    private boolean Begin = false;
    private boolean Stop = false;
    private boolean Save = false;
    private double startTime;
    private double endTime;
    private String totalTime;


    Handler handler = new Handler();
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            totalTime = getTime(startTime);
            //String totalTimeStr = String.valueOf(totalTime);
            mTimerDisplay.setText(totalTime);
            Log.d("Handlers", "Called on main thread");
            // Repeat this the same runnable code block again another 2 seconds
            handler.postDelayed(runnableCode, 20);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.timer);

        mTimerHeader = (TextView) findViewById(R.id.TimerHeader);
        mTimerActions = (TextView) findViewById(R.id.TimerActions);
        mTimerDisplay = (TextView) findViewById(R.id.TimerDisplay);

        mTimerHeader.setText("Timer");
        mTimerActions.setText("Tap to begin");

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
        if(gesture ==Gesture.TAP && !Begin){
            startTime = System.currentTimeMillis();
            Begin = true;
            mTimerActions.setText("(Tap to stop test)");
            //mTimerDisplay.setText("Testing");
            handler.post(runnableCode);
//            Animation anim = new AlphaAnimation(0.0f, 1.0f);
//            anim.setDuration(200); //You can manage the time of the blink with this parameter
//            anim.setStartOffset(20);
//            anim.setRepeatMode(Animation.REVERSE);
//            anim.setRepeatCount(Animation.INFINITE);
//            mTimerDisplay.startAnimation(anim);
            return true;
        }
        else if(gesture == Gesture.TAP && Begin && !Stop){

            //endTime = System.currentTimeMillis();
            //totalTime = (endTime-startTime)/1000.0;
            totalTime = getTime(startTime);
            String totalTimeStr = String.valueOf(totalTime);
            Stop = true;
            //mTimerDisplay.clearAnimation();
            handler.removeCallbacks(runnableCode);
            mTimerActions.setText("(Tap to save result, swipe to start over.)");
            mTimerDisplay.setText(totalTimeStr+" Seconds");
            return true;
        }
        else if(gesture==Gesture.TAP && Begin && Stop && !Save){
            /**
             * TODO: need to add saving here
             */
            Save = true;
            mTimerActions.setText("(Tap for new test)");
            mTimerDisplay.setText("Time saved");
            return true;
        }
        else if (gesture == Gesture.TAP && Begin && Stop && Save){
            startActivity(new Intent(TaskTimer.this, TestSelect.class));
            return true;
        }
        else if (gesture == Gesture.SWIPE_RIGHT && Begin && Stop && !Save){
            startActivity(new Intent(TaskTimer.this, TaskTimer.class));
            return true;
        }
        else{
            return true;
        }
    }
    @Override
    public void onFingerCountChanged(int i, int i1) {

    }

    public String getTime(double sTime){

        double eTime = System.currentTimeMillis();
        double milliseconds = eTime - sTime;
        String totalTime = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours((long) milliseconds)% 24,
                TimeUnit.MILLISECONDS.toMinutes((long) milliseconds) % 60,
                TimeUnit.MILLISECONDS.toSeconds((long) milliseconds)%60);
        return totalTime;
    }

}
