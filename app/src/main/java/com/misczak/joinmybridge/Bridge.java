package com.misczak.joinmybridge;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by misczak on 3/3/15.
 */
public class Bridge {

    private UUID mBridgeId;
    private String mBridgeName;
    private String mBridgeNumber;
    private String mHostCode;
    private String mParticipantCode;
    private String mFirstTone;
    private String mSecondTone;

    private static final String JSON_ID = "id";
    private static final String JSON_NAME = "title";
    private static final String JSON_NUMBER = "number";
    private static final String JSON_HOST = "host";
    private static final String JSON_PARTICIPANT = "participant";
    private static final String JSON_FIRSTTONE = "firsttone";
    private static final String JSON_SECONDTONE = "secondtone";

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


    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mBridgeId.toString());
        json.put(JSON_NAME, mBridgeName.toString());
        json.put(JSON_NUMBER, mBridgeNumber.toString());
        json.put(JSON_HOST, mHostCode.toString());
        json.put(JSON_PARTICIPANT, mParticipantCode.toString());
        json.put(JSON_FIRSTTONE, mFirstTone.toString());
        json.put(JSON_SECONDTONE, mSecondTone.toString());

        return json;
    }

    @Override
    public String toString() {
        return mBridgeName;
    }




}
