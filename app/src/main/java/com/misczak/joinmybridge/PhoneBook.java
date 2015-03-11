package com.misczak.joinmybridge;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by misczak on 3/3/15.
 */
public class PhoneBook {
    private static PhoneBook sPhoneBook;
    private ArrayList<Bridge> mBridgeList;
    private Context mContext;

    private PhoneBook(Context context) {
        mContext = context;
        mBridgeList = new ArrayList<Bridge>();
        /*for (int i = 0; i < 100; i++) {
            Bridge b = new Bridge();
            b.setBridgeName("Bridge #" + i);
            b.setBridgeNumber(i + "");
            b.setHostCode(i + "");
            b.setParticipantCode(i + "");
            b.setFirstTone("#");
            b.setSecondTone("#");
            mBridgeList.add(b);
        }*/
    }

    public static PhoneBook get(Context c){
        if (sPhoneBook == null){
            sPhoneBook = new PhoneBook(c.getApplicationContext());
        }

        return sPhoneBook;
    }

    public void addBridge(Bridge b) {
        mBridgeList.add(b);
    }

    public ArrayList<Bridge> getBridges(){
        return mBridgeList;
    }

    public Bridge getBridge(UUID bridgeId) {
        for (Bridge b: mBridgeList){
            if (b.getBridgeId().equals(bridgeId))
                return b;
        }

        return null;
    }


}
