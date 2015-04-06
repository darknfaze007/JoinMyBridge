package com.misczak.joinmybridge;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by misczak on 3/26/15.
 */
public class SettingsActivity extends ActionBarActivity {

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        /*if (toolbar != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        }*/
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();


    }


}
