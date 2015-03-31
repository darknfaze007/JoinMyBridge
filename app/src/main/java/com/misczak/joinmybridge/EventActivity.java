package com.misczak.joinmybridge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by misczak on 3/30/15.
 */
public class EventActivity extends SingleFragmentActivity {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
            //toolbar.setNavigationIcon(R.drawable.ic_settings_white_24dp);
        }

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

    }


    @Override
    protected Fragment createFragment() {

        long calendarId = (long) getIntent().getLongExtra("calendarId", -1);

        return EventFragment.newInstance(calendarId);
    }
}
