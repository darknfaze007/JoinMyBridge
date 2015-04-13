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

import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;


public class CallUtilities {

    private static final String ENCODED_POUND_SIGN = "%23";
    private static final String ENCODED_STAR_SIGN = "*";
    private static final String[] toneArray = {"#", "*", "#,,*", "*,,*", "*,,#", "#,,#"};

    private static final String TAG = "CallUtilities";

    private static final String HOST_FIRST="Host Code First";

    private String mBridgeNumber, mHostCode, mParticipantCode, mFirstTone, mSecondTone;
    private String mNumberToCall, log;
    private ArrayList<Bridge> mBridgeList;
    private Bridge mBridge;
    private String mFirstPauseTone;
    private String mSecondPauseTone;


    public String getCompleteNumber (UUID bridgeId, ArrayList<Bridge> bridgeList, boolean dialWithParticipant, boolean dialWithHost) {


        mBridgeList = bridgeList;

        for (Bridge b : mBridgeList) {
            if (b.getBridgeId() == bridgeId){
                mBridge = b;
            }
        }

        mFirstPauseTone = getPauseTone(mBridge.getDialingPause());
        mSecondPauseTone = getPauseTone(mBridge.getDialingPause());
        Log.d(TAG, mFirstPauseTone);


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
            mNumberToCall = getNumber();
        }


        log = "Number being called is " + mNumberToCall;
        Log.d(TAG, log);
        return mNumberToCall;

    }



    private String getNumberWithParticipant(UUID bridgeId) {
        return "tel:"
                + mBridge.getBridgeNumber().trim()
                + mFirstPauseTone
                + mBridge.getParticipantCode().trim()
                + encodeToneString(mBridge.getFirstTone().toString());

    }

    private String getNumberWithHost (UUID bridgeId) {
        return "tel:"
                + mBridge.getBridgeNumber().trim()
                + mFirstPauseTone
                + mBridge.getHostCode().trim()
                + encodeToneString(mBridge.getSecondTone().toString());
    }

    private String getNumberWithBoth (UUID bridgeId) {

        if (mBridge.getCallOrder().equals(HOST_FIRST)) {
            return "tel:"
                    + mBridge.getBridgeNumber().trim()
                    + mFirstPauseTone
                    + mBridge.getHostCode().trim()
                    + encodeToneString(mBridge.getSecondTone().toString())
                    + mSecondPauseTone
                    + mBridge.getParticipantCode().trim()
                    + encodeToneString(mBridge.getFirstTone().toString());
        } else {
            return "tel:"
                    + mBridge.getBridgeNumber().trim()
                    + mFirstPauseTone
                    + mBridge.getParticipantCode().trim()
                    + encodeToneString(mBridge.getFirstTone().toString())
                    + mSecondPauseTone
                    + mBridge.getHostCode().trim()
                    + encodeToneString(mBridge.getSecondTone().toString());
        }

    }

    private String getNumber () {
        return "tel:" + mBridge.getBridgeNumber().trim();
    }


    private String encodeToneString(String tone) {

        int tonePosition = 0;


        for (int i = 0; i < toneArray.length; i++){
            if (tone.equals(toneArray[i])){
                tonePosition = i;
                break;
            }
        }

        //If using tones with a pause in between, clear the extra pause after the last tone
        if (tonePosition > 1) {
            mSecondPauseTone = "";
        }

        Log.d(TAG, "Tone Code is " + tonePosition);

        switch (tonePosition){
            case 0:
                return ENCODED_POUND_SIGN;
            case 1:
                return ENCODED_STAR_SIGN;
            case 2:
                return "%23" + mFirstPauseTone + "*";
            case 3:
                return "*" + mFirstPauseTone + "*";
            case 4:
                return "*" + mFirstPauseTone + "%23";
            case 5:
                return "%23" + mFirstPauseTone + "%23";
            default:
                return tone;
        }

    }


    protected String getPauseTone(int pauseLength) {

        String pauseTone = "";

        for (int i = 1; i <= pauseLength; i++) {
            pauseTone += "%2C";
        }

        return pauseTone;
    }
}
