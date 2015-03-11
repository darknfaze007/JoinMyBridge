package com.misczak.joinmybridge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by misczak on 3/3/15.
 */
public abstract class SingleFragmentActivity extends ActionBarActivity {

    protected abstract Fragment createFragment();

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
            //getSupportActionBar().setHomeButtonEnabled(true);
            //getSupportActionBar().setDisplayShowHomeEnabled(true);
            //toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            //toolbar.setLogo(R.drawable.ic_launcher);
        }



        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }


    }

}
