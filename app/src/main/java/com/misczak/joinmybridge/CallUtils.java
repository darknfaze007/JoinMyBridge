package com.misczak.joinmybridge;

import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by misczak on 3/11/15.
 */
public class CallUtils {

    private static final String ENCODED_POUND_SIGN = "%23";
    private static final String ENCODED_STAR_SIGN = "*";
    private static final String TAG = "CallUtils";

    private static final String HOST_FIRST="Host Code First, Then Participant Code";

    private String mBridgeNumber, mHostCode, mParticipantCode, mFirstTone, mSecondTone;
    private String mNumberToCall, log;
    private ArrayList<Bridge> mBridgeList;
    private Bridge mBridge;


    public String getCompleteNumber (UUID bridgeId, ArrayList<Bridge> bridgeList, boolean dialWithParticipant, boolean dialWithHost) {


        mBridgeList = bridgeList;

        for (Bridge b : mBridgeList) {
            if (b.getBridgeId() == bridgeId){
                mBridge = b;
            }
        }

        if (dialWithParticipant == true && dialWithHost == true){
            mNumberToCall = getNumberWithBoth(bridgeId);
        }

        if (dialWithParticipant == true && dialWithHost == false) {
            mNumberToCall = getNumberWithParticipant(bridgeId);
        }

        if (dialWithParticipant == false && dialWithHost == true) {
            mNumberToCall = getNumberWithHost(bridgeId);
        }

        if (dialWithParticipant == false && dialWithHost == false) {
            mNumberToCall = getNumber(bridgeId);
        }


        log = "Number being called is " + mNumberToCall;
        Log.d(TAG, log);
        return mNumberToCall;

    }



    private String getNumberWithParticipant(UUID bridgeId) {
        return "tel:"
                + mBridge.getBridgeNumber().trim()
                + ","
                + mBridge.getParticipantCode().trim()
                + encodeToneString(mBridge.getFirstTone().toString());

    }

    private String getNumberWithHost (UUID bridgeId) {
        return "tel:"
                + mBridge.getBridgeNumber().trim()
                + ","
                + mBridge.getHostCode().trim()
                + encodeToneString(mBridge.getSecondTone().toString());
    }

    private String getNumberWithBoth (UUID bridgeId) {

        if (mBridge.getCallOrder().equals(HOST_FIRST)) {
            return "tel:"
                    + mBridge.getBridgeNumber().trim()
                    + ","
                    + mBridge.getHostCode().trim()
                    + encodeToneString(mBridge.getSecondTone().toString())
                    + ","
                    + mBridge.getParticipantCode().trim()
                    + encodeToneString(mBridge.getFirstTone().toString());
        } else {
            return "tel:"
                    + mBridge.getBridgeNumber().trim()
                    + ","
                    + mBridge.getParticipantCode().trim()
                    + encodeToneString(mBridge.getFirstTone().toString())
                    + ","
                    + mBridge.getHostCode().trim()
                    + encodeToneString(mBridge.getSecondTone().toString());
        }

    }

    private String getNumber (UUID bridgeId) {
        return "tel:" + mBridge.getBridgeNumber().trim();
    }


    private String encodeToneString(String tone) {
        if (tone.equals("#")) {
            return ENCODED_POUND_SIGN;
        } else if (tone.equals("*")) {
            return ENCODED_STAR_SIGN;
        } else {
            return tone;
        }
    }
}
