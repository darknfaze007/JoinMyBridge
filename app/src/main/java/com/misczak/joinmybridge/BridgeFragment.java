package com.misczak.joinmybridge;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.UUID;

/**
 * Created by misczak on 3/3/15.
 */
public class BridgeFragment extends DialogFragment {

    public static final String EXTRA_BRIDGE_ID = "com.misczak.joinmybridge.bridge_id";
    public static final String EXTRA_BRIDGE_NUMBER = "com.misczak.joinmybridge.bridge_number";
    public static final String EXTRA_PARTICIPANT_CODE = "com.misczak.joinmybridge.participant_code";
    public static final String EXTRA_HOST_CODE = "com.misczak.joinmybridge.host_code";
    public static final String EXTRA_BRIDGE_NAME = "com.misczak.joinmybridge.bridge_name";

    private Bridge mBridge;
    private String mBridgeNameString, mBridgeNumberString, mParticipantCodeString, mHostCodeString,
            mFirstToneString, mSecondToneString, mCallOrderString;
    private EditText mBridgeName, mBridgeNumber, mParticipantCode, mHostCode;
    private Spinner mFirstTone, mSecondTone, mCallOrder;
    private UUID mBridgeId;

    private static final String DEFAULT_TONE = "#";
    static final String DEFAULT_ORDER = "Participant Code First";
    private static final int REQUEST_WARNING = 0;
    private static final String DIALOG_WARNING= "warning";
    static final String DEFAULT_FIELD = "None";

    private static final String TAG = "BridgeFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mBridgeId = (UUID)getArguments().getSerializable(EXTRA_BRIDGE_ID);

        if (mBridgeId != null) {
            mBridge = PhoneBook.get(getActivity()).getBridge(mBridgeId);


            mBridgeNameString = mBridge.getBridgeName();
            mBridgeNumberString = mBridge.getBridgeNumber();
            mHostCodeString = mBridge.getHostCode();
            mParticipantCodeString = mBridge.getParticipantCode();

            getActivity().setTitle("Edit Bridge");
        }
        else {
            getActivity().setTitle("New Bridge");
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_bridge, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.menu_item_save_bridge:
                    Log.d(TAG, "Saving bridge");
                    saveBridge();
                    if (NavUtils.getParentActivityName(getActivity()) != null) {
                        NavUtils.navigateUpFromSameTask(getActivity());
                    }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bridge3, parent, false);

        mBridgeName = (EditText)v.findViewById(R.id.bridge_name);
        if (mBridgeId != null && !mBridge.getBridgeName().equals(DEFAULT_FIELD)) {
            mBridgeName.setText(mBridge.getBridgeName());
        } else if (getArguments().getSerializable(EXTRA_BRIDGE_NAME) != null) {
            mBridgeNameString = (getArguments().getSerializable(EXTRA_BRIDGE_NAME)).toString();
            mBridgeName.setText(mBridgeNameString);
        }
        mBridgeName.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int after){
                    mBridgeNameString = c.toString();
                    if (mBridgeNameString.equals("")) {
                        mBridgeNameString = DEFAULT_FIELD;
                    }
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {

            }

            public void afterTextChanged(Editable c) {

            }
        });


        mBridgeNumber = (EditText)v.findViewById(R.id.bridge_number);
        if (mBridgeId != null && !mBridge.getBridgeNumber().equals(DEFAULT_FIELD)) {
            mBridgeNumber.setText(mBridge.getBridgeNumber());
        } else if (getArguments().getSerializable(EXTRA_BRIDGE_NUMBER) != null){
            mBridgeNumberString = (getArguments().getSerializable(EXTRA_BRIDGE_NUMBER).toString());
            mBridgeNumber.setText(mBridgeNumberString);
        }

        mBridgeNumber.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int after){
                    mBridgeNumberString = c.toString();
                    if (mBridgeNumberString.equals("")) {
                        mBridgeNumberString = DEFAULT_FIELD;
                    }

            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //Left blank for now
            }

            public void afterTextChanged(Editable c) {
            }
        });


        mHostCode = (EditText)v.findViewById(R.id.host_code);
        if (mBridgeId != null && !mBridge.getHostCode().equals(DEFAULT_FIELD)) {
            mHostCode.setText(mBridge.getHostCode());
        } else if (getArguments().getSerializable(EXTRA_HOST_CODE) != null){
            mHostCodeString = getArguments().getSerializable(EXTRA_HOST_CODE).toString();
            mHostCode.setText(mHostCodeString);
        }

        mHostCode.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int after){
                    mHostCodeString = c.toString();
                    if (mHostCodeString.equals("")) {
                        mHostCodeString = DEFAULT_FIELD;
                    }
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //Left blank for now
            }

            public void afterTextChanged(Editable c) {
            }
        });


        mParticipantCode = (EditText)v.findViewById(R.id.participant_code);
        if (mBridgeId != null && !mBridge.getParticipantCode().equals(DEFAULT_FIELD)) {
            mParticipantCode.setText(mBridge.getParticipantCode());
        } else if (getArguments().getSerializable(EXTRA_PARTICIPANT_CODE) != null){
            mParticipantCodeString = getArguments().getSerializable(EXTRA_PARTICIPANT_CODE).toString();
            mParticipantCode.setText(mParticipantCodeString);
        }

        mParticipantCode.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence c, int start, int before, int after) {
                mParticipantCodeString = c.toString();
                if (mParticipantCodeString.equals("")) {
                    mParticipantCodeString = DEFAULT_FIELD;
                }
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                //Left blank for now
            }

            public void afterTextChanged(Editable c) {
            }
        });


        mFirstTone = (Spinner)v.findViewById(R.id.bridgeFirstToneKey);

        if (mBridgeId != null && mBridge.getFirstTone() != null){
            mFirstTone.setSelection(getSpinnerIndex(mFirstTone, mBridge.getFirstTone()));
        }

        mFirstTone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFirstToneString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFirstToneString = DEFAULT_TONE;
            }
        });

        mSecondTone = (Spinner)v.findViewById(R.id.bridgeSecondToneKey);

        if (mBridgeId != null && mBridge.getSecondTone() != null){
            mSecondTone.setSelection(getSpinnerIndex(mSecondTone, mBridge.getSecondTone()));
        }

        mSecondTone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSecondToneString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSecondToneString = DEFAULT_TONE;
            }
        });

        mCallOrder = (Spinner)v.findViewById(R.id.callOrderSpinner);

        if (mBridgeId != null && mBridge.getCallOrder() != null){
            mCallOrder.setSelection(getSpinnerIndex(mCallOrder, mBridge.getCallOrder()));
        }

        mCallOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCallOrderString = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCallOrderString = DEFAULT_ORDER;
            }
        });

        return v;
    }

    public static BridgeFragment newInstance(UUID bridgeId, String bridgeName, String bridgeNumber, String participantCode, String hostCode) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BRIDGE_ID, bridgeId);
        args.putSerializable(EXTRA_BRIDGE_NAME, bridgeName);
        args.putSerializable(EXTRA_BRIDGE_NUMBER, bridgeNumber);
        args.putSerializable(EXTRA_PARTICIPANT_CODE, participantCode);
        args.putSerializable(EXTRA_HOST_CODE, hostCode);

        BridgeFragment fragment = new BridgeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private int getSpinnerIndex(Spinner spinner, String tone) {
        int index = 0;

        for (int i=0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(tone)) {
                index = i;
            }
        }
        return index;
    }

    private void saveBridge() {

        Bridge b;

        if (mBridgeId != null)
            b = PhoneBook.get(getActivity()).getBridge(mBridgeId);
        else {
            b = new Bridge();
            PhoneBook.get(getActivity()).addBridge(b);
        }

        if (mBridgeNameString != null)
            b.setBridgeName(mBridgeNameString);
        else
            b.setBridgeName(DEFAULT_FIELD);

        if (mBridgeNumberString != null)
            b.setBridgeNumber(mBridgeNumberString);
        else
            b.setBridgeNumber(DEFAULT_FIELD);

        if (mHostCodeString != null)
            b.setHostCode(mHostCodeString);
        else
            b.setHostCode(DEFAULT_FIELD);

        if (mParticipantCodeString != null)
            b.setParticipantCode(mParticipantCodeString);
        else
            b.setParticipantCode(DEFAULT_FIELD);

        b.setFirstTone(mFirstToneString);
        b.setSecondTone(mSecondToneString);
        b.setCallOrder(mCallOrderString);

        PhoneBook.get(getActivity()).savePhoneBook();

    }

    private boolean validateBridgeData() {
        if (mBridgeNameString == null || mBridgeNumberString == null ||
                mHostCodeString == null || mParticipantCodeString == null) {
            return false;
        }
        else {
            return true;
        }
    }


}
