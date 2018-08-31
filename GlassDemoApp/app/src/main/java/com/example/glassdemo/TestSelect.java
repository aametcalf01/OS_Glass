package com.example.glassdemo;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

public class TestSelect extends Activity{

    //Index of cards
    static final int TIMER = 0;
    static final int CAMERA = 1;
    static final int VOICE = 2;
    static final int BARCODE = 3;

    private CardScrollAdapter mAdapter;
    private CardScrollView mCardScroller;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mAdapter = new CardAdapter(createCards(this));
        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(mAdapter);
        setContentView(mCardScroller);
        setCardScrollerListener();
    }

    /**
     * Create a list of the cards
     */
    private List<CardBuilder> createCards(Context context) {
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();

        cards.add(TIMER, new CardBuilder(context, CardBuilder.Layout.COLUMNS)
                .setText("Timer Test")
                .setFootnote(R.string.test_select_footnote)
                .setIcon(R.drawable.timer));

        cards.add(CAMERA, new CardBuilder(context, CardBuilder.Layout.COLUMNS)
                .setText("Camera Test")
                .setFootnote(R.string.test_select_footnote)
                .setIcon(R.drawable.camera));

        cards.add(VOICE, new CardBuilder(context, CardBuilder.Layout.COLUMNS)
                .setText("Voice Recognition Test")
                .setFootnote(R.string.test_select_footnote)
                .setIcon(R.drawable.voice));

        cards.add(BARCODE, new CardBuilder(context, CardBuilder.Layout.COLUMNS)
                .setText("Barcode Reader Test")
                .setFootnote(R.string.test_select_footnote)
                .setIcon(R.drawable.barcode));

        return cards;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mCardScroller.activate();
    }

    @Override
    protected void onPause() {
        mCardScroller.deactivate();
        super.onPause();
    }
    /**
     * This function will direct to the Test1 class on tap gesture during beta run
     * TODO add barcode scanner
     */
    private void setCardScrollerListener(){
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int soundEffect = Sounds.TAP;
                switch (position){
                    case TIMER:
                        startActivity(new Intent(TestSelect.this, TimerIntro.class));
                        break;
                    case CAMERA:
                        startActivity(new Intent(TestSelect.this, TaskCamera.class));
                        break;
                    case VOICE:
                        startActivity(new Intent(TestSelect.this, TaskVoice.class));
                        break;
                    case BARCODE:
                        startActivity(new Intent(TestSelect.this,MainActivity.class));
                        break;
                    default:
                        soundEffect = Sounds.ERROR;
                }
                AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                am.playSoundEffect(soundEffect);
            }
        });
    }
}