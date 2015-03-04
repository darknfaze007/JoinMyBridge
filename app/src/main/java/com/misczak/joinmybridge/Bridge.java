package com.misczak.joinmybridge;

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

    protected static final String NAME_PREFIX = "Name_";
    protected static final String NUMBER_PREFIX= "Number_";
    protected static final String HOST_PREFIX = "Host_";
    protected static final String PIN_PREFIX = "Participant_";


    public Bridge() {
        mBridgeId = UUID.randomUUID();
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

    @Override
    public String toString() {
        return mBridgeName;
    }




}
