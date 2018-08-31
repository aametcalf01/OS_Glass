package com.example.glassdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.glass.view.WindowUtils;
import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

public class TaskVoice extends Activity {
    private CardScrollView mCardScroller;

    private int mPicture = 0;

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

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            getMenuInflater().inflate(R.menu.voice_menu, menu);
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
                case R.id.menu_vehicles:
                    mPicture = 0;
                    break;
                case R.id.menu_utility:
                    mPicture = 1;
                    break;
                case R.id.menu_carrier:
                    mPicture = 2;
                    break;
                case R.id.menu_lifts:
                    mPicture = 3;
                    break;
                case R.id.menu_scissor:             mPicture = 4; break;
                case R.id.menu_engine_powered_boom: mPicture = 5; break;
                case R.id.menu_electric_boom:       mPicture = 6; break;
                case R.id.menu_vertical:            mPicture = 7; break;
                default: return true;  // No change.
            }
            mCardScroller.setAdapter(new CardAdapter(createCards(this)));
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    /**
     * Creates a singleton card list to display as activity content.
     */
    private List<CardBuilder> createCards(Context context) {
        ArrayList<CardBuilder> cards = new ArrayList<CardBuilder>();
        CardBuilder card = new CardBuilder(context, CardBuilder.Layout.COLUMNS)
                .addImage(getImageResource())
                .setText(R.string.voice_menu_explanation);
        cards.add(card);
        return cards;
    }

    /** Returns current image resource. */
    private int getImageResource() {
        switch (mPicture) {
            case 1:  return R.drawable.utility_vehicle;
            case 2:  return R.drawable.carrier;
            case 3:  return R.drawable.scissor;
            case 4:  return R.drawable.scissor;
            case 5:  return R.drawable.engine_power_boom;
            case 6:  return R.drawable.electric_boom;
            case 7:  return R.drawable.vertical;
            default: return R.drawable.utility_vehicle;
        }
    }


}
