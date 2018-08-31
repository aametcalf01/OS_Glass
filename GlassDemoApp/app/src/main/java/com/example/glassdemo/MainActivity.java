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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    //Index of cards
    static final int TITLE = 0;

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
    private List<CardBuilder> createCards(Context context){
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();

        cards.add(TITLE, new CardBuilder(context, CardBuilder.Layout.COLUMNS)
                .setText(R.string.title_text)
                .setFootnote(R.string.title_footnote)
                .setIcon(R.drawable.osi_logo));
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

    private void setCardScrollerListener(){
        mCardScroller.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int soundEffect = Sounds.TAP;
                switch (position){
                    case TITLE:
                        soundEffect = Sounds.SELECTED;
                        startActivity(new Intent(MainActivity.this, TestSelect.class));
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
