package com.misczak.joinmybridge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by misczak on 3/3/15.
 */
public class BridgeFragment extends Fragment {

    private Bridge mBridge;
    private EditText mBridgeName, mBridgeNumber, mParticipantCode, mHostCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBridge = new Bridge();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bridge, parent, false);

        mBridgeName = (EditText)v.findViewById(R.id.bridge_name);
        mBridgeName.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int after){
                mBridge.setBridgeName(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //Left blank for now
            }

            public void afterTextChanged(Editable c) {
            }
        });


        mBridgeNumber = (EditText)v.findViewById(R.id.bridge_number);
        mBridgeNumber.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int after){
                mBridge.setBridgeNumber(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //Left blank for now
            }

            public void afterTextChanged(Editable c) {
            }
        });


        mHostCode = (EditText)v.findViewById(R.id.host_code);
        mHostCode.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int after){
                mBridge.setHostCode(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //Left blank for now
            }

            public void afterTextChanged(Editable c) {
            }
        });


        mParticipantCode = (EditText)v.findViewById(R.id.participant_code);
        mParticipantCode.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int after){
                mBridge.setParticipantCode(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //Left blank for now
            }

            public void afterTextChanged(Editable c) {
            }
        });






        return v;
    }


}
