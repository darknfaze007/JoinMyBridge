package com.misczak.joinmybridge;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by misczak on 3/3/15.
 */
public class PhoneBook {
    private static final String TAG = "PhoneBook";
    private static final String FILENAME = "phonebook.json";

    private static PhoneBook sPhoneBook;
    private ArrayList<Bridge> mBridgeList;
    private BridgeJSONSerializer mSerializer;
    private Context mContext;





    private PhoneBook(Context context) {
        mContext = context;

        mSerializer = new BridgeJSONSerializer(mContext, FILENAME);

        try {
            mBridgeList = mSerializer.loadPhoneBook();
        } catch (Exception e) {
            mBridgeList = new ArrayList<Bridge>();
            Log.e(TAG, "Error loading PhoneBook: ", e);
        }


        //mBridgeList = new ArrayList<Bridge>();
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

    public void deleteBridge(Bridge b) { mBridgeList.remove(b); }

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


    public boolean savePhoneBook() {
        try {
            mSerializer.savePhoneBook(mBridgeList);
            Log.d(TAG, "PhoneBook saved to file");
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "Error saving PhoneBook: ", e);
            return false;
        }
    }


}
