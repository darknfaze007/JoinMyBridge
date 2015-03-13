package com.misczak.joinmybridge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import java.util.UUID;


public class BridgeActivity extends SingleFragmentActivity {

    //private Toolbar toolbar;


    @Override
    protected Fragment createFragment() {
        UUID bridgeId = (UUID)getIntent().getSerializableExtra(BridgeFragment.EXTRA_BRIDGE_ID);

        return BridgeFragment.newInstance(bridgeId);
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        if (toolbar != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            //toolbar.setNavigationIcon(R.drawable.ic_settings_white_24dp);
        }

    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

        FragmentManager fm = getSupportFragmentManager();
        Fragment bridgeFragment = fm.findFragmentById(R.id.fragment_container);

        if (bridgeFragment == null){
            bridgeFragment = new BridgeFragment();
            fm.beginTransaction().add(R.id.fragment_container, bridgeFragment).commit();
        }*/

        /*NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                fm.findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setup(R.id.fragment_navigation_drawer, (DrawerLayout)findViewById(R.id.drawer_layout), toolbar);


        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState == null) {

                PhoneBookFragment pbFragment = new PhoneBookFragment();

                pbFragment.setArguments(getIntent().getExtras());

                getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, pbFragment)
                    .commit();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}


