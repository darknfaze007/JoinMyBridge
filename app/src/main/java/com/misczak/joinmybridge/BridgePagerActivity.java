package com.misczak.joinmybridge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by misczak on 3/5/15.
 */
public class BridgePagerActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ArrayList<Bridge> mBridgeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mBridgeList = PhoneBook.get(this).getBridges();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Bridge bridge = mBridgeList.get(position);
                return BridgeFragment.newInstance(bridge.getBridgeId());
            }

            @Override
            public int getCount() {
                return mBridgeList.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged (int state) { }

            public void onPageScrolled (int pos, float posOffset, int posOffsetPixels) { }

            //Not used for anything currently
            public void onPageSelected(int pos) {
                Bridge bridge = mBridgeList.get(pos);
                if (bridge.getBridgeName() != null) {
                    setTitle(bridge.getBridgeName());
                }
            }
        });


        UUID bridgeId = (UUID)getIntent().getSerializableExtra(BridgeFragment.EXTRA_BRIDGE_ID);

        for (int i = 0; i < mBridgeList.size(); i++){
            if (mBridgeList.get(i).getBridgeId().equals(bridgeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }



    }




}
