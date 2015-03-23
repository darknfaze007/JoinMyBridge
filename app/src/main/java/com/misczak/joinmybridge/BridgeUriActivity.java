package com.misczak.joinmybridge;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by misczak on 3/22/15.
 */
public class BridgeUriActivity extends SingleFragmentActivity {

    Intent uriIntent;
    String uriBridgeNumber, uriBridgeParticipantCode, uriBridgeHostCode;
    String tones = "[#*]";
    String[] participantComponents, hostComponents;

    @Override
    protected Fragment createFragment() {

        uriIntent = getIntent();

        if (Intent.ACTION_VIEW.equals(uriIntent.getAction())) {
            Uri bridgeUri = uriIntent.getData();
            uriBridgeNumber = bridgeUri.getQueryParameter("number");
            uriBridgeParticipantCode = bridgeUri.getQueryParameter("participant");
            uriBridgeHostCode = bridgeUri.getQueryParameter("host");
        }

        if (uriBridgeNumber != null){
        }

        if (uriBridgeParticipantCode != null) {
            participantComponents = uriBridgeParticipantCode.split(tones);
        }

        if (uriBridgeHostCode != null) {
            hostComponents = uriBridgeParticipantCode.split(tones);
        }

        return BridgeUriFragment.newInstance(uriBridgeNumber, participantComponents[0],
                participantComponents[1], hostComponents[0], hostComponents[1]);
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);

        }

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

    }


}
