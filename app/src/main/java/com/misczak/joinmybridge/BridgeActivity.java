/*
 * Copyright 2015 John Misczak
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.misczak.joinmybridge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

import java.util.UUID;


public class BridgeActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        UUID bridgeId = (UUID)getIntent().getSerializableExtra(BridgeFragment.EXTRA_BRIDGE_ID);
        String bridgeName = (String)getIntent().getSerializableExtra(BridgeFragment.EXTRA_BRIDGE_NAME);
        String bridgeNumber = (String)getIntent().getSerializableExtra(BridgeFragment.EXTRA_BRIDGE_NUMBER);
        String participantCode = (String)getIntent().getSerializableExtra(BridgeFragment.EXTRA_PARTICIPANT_CODE);
        String hostCode = (String)getIntent().getSerializableExtra(BridgeFragment.EXTRA_HOST_CODE);

        return BridgeFragment.newInstance(bridgeId, bridgeName, bridgeNumber, participantCode, hostCode);
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


