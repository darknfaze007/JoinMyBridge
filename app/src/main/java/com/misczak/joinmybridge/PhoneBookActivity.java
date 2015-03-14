package com.misczak.joinmybridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by misczak on 3/3/15.
 */
public class PhoneBookActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PhoneBookFragment();
    }


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Bridge bridge = new Bridge();
                //PhoneBook.get(getApplicationContext()).addBridge(bridge);
                Intent i = new Intent(getApplicationContext(), BridgeActivity.class);
                //i.putExtra(BridgeFragment.EXTRA_BRIDGE_ID, bridge.getBridgeId());
                startActivityForResult(i, 0);
            }
        });



    }

}
