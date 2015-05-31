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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Bridge {

    private UUID mBridgeId;
    private String mBridgeName;
    private String mBridgeNumber;
    private String mHostCode;
    private String mParticipantCode;
    private String mFirstTone;
    private String mSecondTone;
    private String mCallOrder;
    private int mDialingPause;
    private int mDialOptions;

    private static final String JSON_ID = "id";
    private static final String JSON_NAME = "title";
    private static final String JSON_NUMBER = "number";
    private static final String JSON_HOST = "host";
    private static final String JSON_PARTICIPANT = "participant";
    private static final String JSON_FIRSTTONE = "firsttone";
    private static final String JSON_SECONDTONE = "secondtone";
    private static final String JSON_CALLORDER = "callorder";
    private static final String JSON_DIALINGPAUSE = "dialingpause";
    private static final String JSON_DIALOPTIONS = "dialoptions";

    /*
    protected static final String NAME_PREFIX = "Name_";
    protected static final String NUMBER_PREFIX= "Number_";
    protected static final String HOST_PREFIX = "Host_";
    protected static final String PIN_PREFIX = "Participant_";
*/

    public Bridge() {
        mBridgeId = UUID.randomUUID();
    }

    public Bridge(JSONObject json) throws JSONException {
        mBridgeId = UUID.fromString(json.getString(JSON_ID));
        mBridgeName = json.getString(JSON_NAME);
        mBridgeNumber = json.getString(JSON_NUMBER);
        mHostCode = json.getString(JSON_HOST);
        mParticipantCode = json.getString(JSON_PARTICIPANT);
        mFirstTone = json.getString(JSON_FIRSTTONE);
        mSecondTone = json.getString(JSON_SECONDTONE);
        mCallOrder = json.getString(JSON_CALLORDER);
        mDialingPause = json.getInt(JSON_DIALINGPAUSE);
        mDialOptions = json.getInt(JSON_DIALOPTIONS);
    }


    public UUID getBridgeId() {
        return mBridgeId;
    }

    public void setBridgeId(UUID bridgeId) {
        this.mBridgeId = bridgeId;
    }

    public String getBridgeName() {
        return mBridgeName;
    }

    public void setBridgeName(String bridgeName) {
        this.mBridgeName = bridgeName;
    }

    public String getBridgeNumber() {
        return mBridgeNumber;
    }

    public void setBridgeNumber(String bridgeNumber) {
        this.mBridgeNumber = bridgeNumber;
    }

    public String getHostCode() {
        return mHostCode;
    }

    public void setHostCode(String hostCode) {
        this.mHostCode = hostCode;
    }

    public String getParticipantCode() {
        return mParticipantCode;
    }

    public void setParticipantCode(String participantCode) {
        this.mParticipantCode = participantCode;
    }

    public String getFirstTone() {
        return mFirstTone;
    }

    public void setFirstTone(String firstTone) {
        this.mFirstTone = firstTone;
    }

    public String getSecondTone() {
        return mSecondTone;
    }

    public void setSecondTone(String secondTone) {
        this.mSecondTone = secondTone;
    }

    public String getCallOrder() { return mCallOrder; }

    public void setCallOrder(String callOrder) { this.mCallOrder = callOrder; }

    public int getDialingPause() { return mDialingPause; }

    public void setDialingPause(int dialingPause) {this.mDialingPause = dialingPause; }

    public int getDialOptions() { return mDialOptions; }

    public void setDialOptions(int dialOptions) { this.mDialOptions = dialOptions; }


    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mBridgeId.toString());
        json.put(JSON_NAME, mBridgeName);
        json.put(JSON_NUMBER, mBridgeNumber);
        json.put(JSON_HOST, mHostCode);
        json.put(JSON_PARTICIPANT, mParticipantCode);
        json.put(JSON_FIRSTTONE, mFirstTone);
        json.put(JSON_SECONDTONE, mSecondTone);
        json.put(JSON_CALLORDER, mCallOrder);
        json.put(JSON_DIALINGPAUSE, mDialingPause);
        json.put(JSON_DIALOPTIONS, mDialOptions);

        return json;
    }

    @Override
    public String toString() {
        return mBridgeName;
    }


}
