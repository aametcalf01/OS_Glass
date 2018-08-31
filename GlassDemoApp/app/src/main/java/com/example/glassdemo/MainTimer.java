package com.example.glassdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static java.security.AccessController.getContext;


public class MainTimer extends Activity
        implements GestureDetector.BaseListener, GestureDetector.FingerListener{

    private TextView mTaskHeader;
    private TextView mTimer;
    private TextView mInstructions;
    private TextView mTime1;
    private TextView mTime2;
    private TextView mTime3;
    private TextView mTime4;
    private TextView mTime5;
    private TextView mTime6;

    private TextView mTableTitle;
    private TextView mTask1;
    private TextView mTask2;
    private TextView mTask3;
    private TextView mTask4;
    private TextView mTask5;
    private TextView mTask6;
    private TextView mTask1Result;
    private TextView mTask2Result;
    private TextView mTask3Result;
    private TextView mTask4Result;
    private TextView mTask5Result;
    private TextView mTask6Result;

    private TextView mTaskHeader1;
    private TextView mTimer1;
    private TextView mInstructions1;
    private double startTime;
    private double endTime;
    private String totalTime;
    private int taskNum;
    private GestureDetector mGestureDetector;
    private boolean startTask;
    private String totalTimeStr;
    private String t1;
    private String t2;
    private String t3;
    private String t4;
    private String t5;
    private String t6;

    private boolean Begin = false;
    private boolean Stop = false;

    Handler handler = new Handler();
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            //The first view is different
            if (taskNum==1){
                totalTime = getTime(startTime);
                mTimer1.setText(totalTime);
                handler.postDelayed(runnableCode, 20);}
                else{
                totalTime = getTime(startTime);
                mTimer.setText(totalTime);
                handler.postDelayed(runnableCode,20);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        taskNum = 1;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);
        setContentView(R.layout.main_timer1);

        mTaskHeader1 = (TextView) findViewById(R.id.TaskHeader1);
        mTimer1 = (TextView) findViewById(R.id.Timer1);
        mInstructions1 = (TextView) findViewById(R.id.Instructions1);

        mTaskHeader1.setText("Task "+ String.valueOf(taskNum));
        mTimer1.setText("00:00:00");
        mInstructions1.setText(R.string.begin_timer);

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
        if(gesture==Gesture.TAP && Begin==false && Stop==false && taskNum==1){
            startTime = System.currentTimeMillis();
            handler.post(runnableCode);
            mInstructions1.setText(R.string.stop_timer);
            Begin = true;
            return true;
        }
        else if(gesture==Gesture.TAP && Begin==true && Stop==false && taskNum==1){
            handler.removeCallbacks(runnableCode);
            totalTime = getTime(startTime);
            totalTimeStr = String.valueOf(totalTime);
            mTimer1.setText(totalTimeStr);
            t1 = totalTimeStr;
            mInstructions1.setText(R.string.next_task);
            Stop = true;
            return true;
        }
        else if(gesture==Gesture.TAP && Begin==true && Stop==true && taskNum==1){
            setContentView(R.layout.main_timer);

            mTaskHeader = (TextView) findViewById(R.id.TaskHeader);
            mTimer = (TextView) findViewById(R.id.Timer);
            mInstructions = (TextView) findViewById(R.id.Instructions);

            mTime1 = (TextView) findViewById(R.id.Time1);
            mTime2 = (TextView) findViewById(R.id.Time2);
            mTime3 = (TextView) findViewById(R.id.Time3);
            mTime4 = (TextView) findViewById(R.id.Time4);
            mTime5 = (TextView) findViewById(R.id.Time5);
            mTime6 = (TextView) findViewById(R.id.Time6);

            mTaskHeader.setText("Task "+String.valueOf(taskNum+1));
            mTimer.setText("00:00:00");
            mInstructions.setText(R.string.begin_timer);
            mTime1.setText("T"+String.valueOf(taskNum)+" "+totalTimeStr);
            taskNum = taskNum+1;
            Begin = false;
            Stop = false;
            return true;
        }
        else if(gesture==Gesture.TAP && Begin==false && Stop==false && taskNum >1 && taskNum<6){
            startTime = System.currentTimeMillis();
            handler.post(runnableCode);
            mInstructions.setText(R.string.stop_timer);
            Begin = true;
            return true;
        }
        else if(gesture==Gesture.TAP && Begin==true && Stop==false && taskNum >1 && taskNum<6){
            handler.removeCallbacks(runnableCode);
            totalTime = getTime(startTime);
            totalTimeStr = String.valueOf(totalTime);
            mTimer.setText(totalTimeStr);
            mInstructions.setText(R.string.next_task);
            Stop = true;
            return true;
        }
        else if (gesture==Gesture.TAP && Begin==true && Stop==true && taskNum >1 && taskNum<6){
            mTaskHeader.setText("Task "+String.valueOf(taskNum+1));
            mTimer.setText("00:00:00");
            mInstructions.setText(R.string.begin_timer);

            if (taskNum==2){
                mTime2.setText("T"+String.valueOf(taskNum)+" "+totalTimeStr);
                t2 = totalTimeStr;
            }
            else if (taskNum==3){
                mTime3.setText("T"+String.valueOf(taskNum)+" "+totalTimeStr);
                t3 = totalTimeStr;
            }
            else if (taskNum==4){
                mTime4.setText("T"+String.valueOf(taskNum)+" "+totalTimeStr);
                t4 = totalTimeStr;
            }
            else if (taskNum==5){
                mTime5.setText("T"+String.valueOf(taskNum)+" "+totalTimeStr);
                t5 = totalTimeStr;
            }

            taskNum = taskNum+1;
            Begin = false;
            Stop = false;
            return true;
        }
        else if(gesture==Gesture.TAP && Begin==false && Stop==false && taskNum==6){
            startTime = System.currentTimeMillis();
            handler.post(runnableCode);
            mInstructions.setText(R.string.stop_timer);
            Begin = true;
            return true;
        }
        else if(gesture==Gesture.TAP && Begin==true && Stop==false && taskNum==6){
            handler.removeCallbacks(runnableCode);
            totalTime = getTime(startTime);
            totalTimeStr = String.valueOf(totalTime);
            mTimer.setText(totalTimeStr);
            t6 = totalTimeStr;
            mInstructions.setText(R.string.show_results);
            Stop =true;
            return true;
        }
        else if (gesture==Gesture.TAP && Begin==true && Stop==true && taskNum==6){
            setContentView(R.layout.timer_results);
            mTableTitle = (TextView) findViewById(R.id.table_title);
            mTask1 = (TextView) findViewById(R.id.task1);
            mTask2 = (TextView) findViewById(R.id.task2);
            mTask3 = (TextView) findViewById(R.id.task3);
            mTask4 = (TextView) findViewById(R.id.task4);
            mTask5 = (TextView) findViewById(R.id.task5);
            mTask6 = (TextView) findViewById(R.id.task6);

            mTask1Result = (TextView) findViewById(R.id.task1Result);
            mTask2Result = (TextView) findViewById(R.id.task2Result);
            mTask3Result = (TextView) findViewById(R.id.task3Result);
            mTask4Result = (TextView) findViewById(R.id.task4Result);
            mTask5Result = (TextView) findViewById(R.id.task5Result);
            mTask6Result = (TextView) findViewById(R.id.task6Result);

            mTableTitle.setText("Timer Results");
            mTask1.setText("Task 1:");
            mTask2.setText("Task 2:");
            mTask3.setText("Task 3:");
            mTask4.setText("Task 4:");
            mTask5.setText("Task 5:");
            mTask6.setText("Task 6:");

            mTask1Result.setText(t1);
            mTask2Result.setText(t2);
            mTask3Result.setText(t3);
            mTask4Result.setText(t4);
            mTask5Result.setText(t5);
            mTask6Result.setText(t6);
            taskNum = taskNum+1;

            return true;
        }
        else if(gesture == Gesture.TAP && taskNum==7){
            startActivity(new Intent(MainTimer.this,TestSelect.class));
            return true;
        }
        else if(gesture==Gesture.SWIPE_DOWN){
            return false;
        }
        else return true;
    }

    @Override
    public void onFingerCountChanged(int i, int i1) {

    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            getMenuInflater().inflate(R.menu.task_start_stop_menu, menu);
            return true;
        }
        // Good practice to pass through, for options menu.
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS && taskNum==1) {
            switch (item.getItemId()) {
                case R.id.start_task:
                   startTime = System.currentTimeMillis();
                   handler.post(runnableCode);
                    mInstructions1.setText(R.string.stop_timer);
                    Begin = true;
                   break;
                case R.id.stop_task:
                    handler.removeCallbacks(runnableCode);
                    totalTime = getTime(startTime);
                    totalTimeStr = String.valueOf(totalTime);
                    mTimer1.setText(totalTimeStr);
                    t1 = totalTimeStr;
                    mInstructions1.setText(R.string.next_task);
                    Stop = true;
                    break;
                case R.id.next_task:
                    setContentView(R.layout.main_timer);

                    mTaskHeader = (TextView) findViewById(R.id.TaskHeader);
                    mTimer = (TextView) findViewById(R.id.Timer);
                    mInstructions = (TextView) findViewById(R.id.Instructions);

                    mTime1 = (TextView) findViewById(R.id.Time1);
                    mTime2 = (TextView) findViewById(R.id.Time2);
                    mTime3 = (TextView) findViewById(R.id.Time3);
                    mTime4 = (TextView) findViewById(R.id.Time4);
                    mTime5 = (TextView) findViewById(R.id.Time5);
                    mTime6 = (TextView) findViewById(R.id.Time6);

                    mTaskHeader.setText("Task "+String.valueOf(taskNum+1));
                    mTimer.setText("00:00:00");
                    mInstructions.setText(R.string.begin_timer);
                    mTime1.setText("T"+String.valueOf(taskNum)+" "+totalTimeStr);
                    taskNum = taskNum+1;
                    Begin = false;
                    Stop = false;
                    break;



                default: return true;  // No change.
            }
            return true;
        }
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS && taskNum >1 && taskNum<6){
            switch (item.getItemId()){
                case R.id.start_task:
                    startTime = System.currentTimeMillis();
                    handler.post(runnableCode);
                    mInstructions.setText(R.string.stop_timer);
                    Begin = true;
                    break;
                case R.id.stop_task:
                    handler.removeCallbacks(runnableCode);
                    totalTime = getTime(startTime);
                    totalTimeStr = String.valueOf(totalTime);
                    mTimer.setText(totalTimeStr);
                    mInstructions.setText(R.string.next_task);
                    Stop = true;
                    break;
                case R.id.next_task:
                    mTaskHeader.setText("Task "+String.valueOf(taskNum+1));
                    mTimer.setText("00:00:00");
                    mInstructions.setText(R.string.begin_timer);

                    if (taskNum==2){
                        mTime2.setText("T"+String.valueOf(taskNum)+" "+totalTimeStr);
                        t2 = totalTimeStr;
                    }
                    else if (taskNum==3){
                        mTime3.setText("T"+String.valueOf(taskNum)+" "+totalTimeStr);
                        t3 = totalTimeStr;
                    }
                    else if (taskNum==4){
                        mTime4.setText("T"+String.valueOf(taskNum)+" "+totalTimeStr);
                        t4 = totalTimeStr;
                    }
                    else if (taskNum==5){
                        mTime5.setText("T"+String.valueOf(taskNum)+" "+totalTimeStr);
                        t5 = totalTimeStr;
                    }

                    taskNum = taskNum+1;
                    Begin = false;
                    Stop = false;
                    break;
                default: return true;  // No change.
            }
        }
        if(featureId == WindowUtils.FEATURE_VOICE_COMMANDS && taskNum==6){
            switch (item.getItemId()){
                case R.id.start_task:
                    startTime = System.currentTimeMillis();
                    handler.post(runnableCode);
                    mInstructions.setText(R.string.stop_timer);
                    Begin=true;
                    break;
                case R.id.stop_task:
                    handler.removeCallbacks(runnableCode);
                    totalTime = getTime(startTime);
                    totalTimeStr = String.valueOf(totalTime);
                    mTimer.setText(totalTimeStr);
                    t6 = totalTimeStr;
                    mInstructions.setText(R.string.show_results);
                    Stop = true;
                    break;
                case R.id.show_results:
                    setContentView(R.layout.timer_results);
                    mTableTitle = (TextView) findViewById(R.id.table_title);
                    mTask1 = (TextView) findViewById(R.id.task1);
                    mTask2 = (TextView) findViewById(R.id.task2);
                    mTask3 = (TextView) findViewById(R.id.task3);
                    mTask4 = (TextView) findViewById(R.id.task4);
                    mTask5 = (TextView) findViewById(R.id.task5);
                    mTask6 = (TextView) findViewById(R.id.task6);

                    mTask1Result = (TextView) findViewById(R.id.task1Result);
                    mTask2Result = (TextView) findViewById(R.id.task2Result);
                    mTask3Result = (TextView) findViewById(R.id.task3Result);
                    mTask4Result = (TextView) findViewById(R.id.task4Result);
                    mTask5Result = (TextView) findViewById(R.id.task5Result);
                    mTask6Result = (TextView) findViewById(R.id.task6Result);

                    mTableTitle.setText("Timer Results");
                    mTask1.setText("Task 1:");
                    mTask2.setText("Task 2:");
                    mTask3.setText("Task 3:");
                    mTask4.setText("Task 4:");
                    mTask5.setText("Task 5:");
                    mTask6.setText("Task 6:");

                    mTask1Result.setText(t1);
                    mTask2Result.setText(t2);
                    mTask3Result.setText(t3);
                    mTask4Result.setText(t4);
                    mTask5Result.setText(t5);
                    mTask6Result.setText(t6);

                    Begin = false;
                    Stop = false;
                    taskNum = taskNum+1;
                    break;


                default:
                    break;
            }
        }
        return super.onMenuItemSelected(featureId, item);
    }



    public String getTime(double sTime){

        double eTime = System.currentTimeMillis();
        double milliseconds = eTime - sTime;
        String totalTime = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) milliseconds),
                TimeUnit.MILLISECONDS.toSeconds((long) milliseconds)%60,
                (TimeUnit.MILLISECONDS.toMillis((long) milliseconds)/10)%100);
        return totalTime;
    }
}
