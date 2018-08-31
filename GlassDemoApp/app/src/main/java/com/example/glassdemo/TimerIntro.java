package com.example.glassdemo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class TimerIntro extends Activity {
    //Index of cards
    static final int MAIN_CARD = 0;
    private CardScrollView mCardScroller;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        // Requests a voice menu on this activity
        // Must make sure to request this before setContentView() is called
        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);

        // Ensure screen stays on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Sets up a singleton card scroller as content of this activity
        mCardScroller = new CardScrollView(this);
        mCardScroller.setAdapter(new CardAdapter(createCards(this)));
        setContentView(mCardScroller);
        // To handle the tap event
        setCardScrollerListener();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            getMenuInflater().inflate(R.menu.timer_intro_menu, menu);
            return true;
        }
        // Good practice to pass through, for options menu.
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            System.out.println("*********in onMenuItemSelect");
            switch (item.getItemId()) {
                case R.id.to_timer_menu:
                    startActivity(new Intent(TimerIntro.this, MainTimer.class));
                    break;

                default: return true;  // No change.
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
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
     * Creates a singleton card list to display as activity content.
     */
    private List<CardBuilder> createCards(Context context) {
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();
        CardBuilder card = new CardBuilder(context, CardBuilder.Layout.COLUMNS)
                .addImage(R.drawable.timer)
                .setText(R.string.begin_timer_demo);
        cards.add(MAIN_CARD,card);
        return cards;
    }

    private void setCardScrollerListener(){
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int soundEffect = Sounds.TAP;
                switch (position){
                    case MAIN_CARD:
                        soundEffect = Sounds.SELECTED;
                        startActivity(new Intent(TimerIntro.this, MainTimer.class));
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
