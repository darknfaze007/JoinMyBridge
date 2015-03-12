package com.misczak.joinmybridge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
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
public class BridgeFragment extends Fragment {

    public static final String EXTRA_BRIDGE_ID = "com.misczak.joinmybridge.bridge_id";

    private Bridge mBridge;
    private String mBridgeNameString;
    private EditText mBridgeName, mBridgeNumber, mParticipantCode, mHostCode;
    private Spinner mFirstTone, mSecondTone, mCallOrder;

    private static final String DEFAULT_TONE = "#";
    //private static final String DEFAULT_ORDER = "Participant Code First, Then Host Code";
    private static final String DEFAULT_ORDER = "Host Code First, Then Participant Code";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID bridgeId = (UUID)getArguments().getSerializable(EXTRA_BRIDGE_ID);
        mBridge = PhoneBook.get(getActivity()).getBridge(bridgeId);

        mBridgeNameString = mBridge.getBridgeName();

        if (mBridgeNameString != null) {
            getActivity().setTitle(mBridgeNameString);
        }
        else {
            getActivity().setTitle("New Bridge");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_bridge_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        PhoneBook.get(getActivity()).savePhoneBook();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bridge, parent, false);

        mBridgeName = (EditText)v.findViewById(R.id.bridge_name);
        mBridgeName.setText(mBridge.getBridgeName());
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
        mBridgeNumber.setText(mBridge.getBridgeNumber());
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
        mHostCode.setText(mBridge.getHostCode());
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
        mParticipantCode.setText(mBridge.getParticipantCode());
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


        mFirstTone = (Spinner)v.findViewById(R.id.bridgeFirstToneKey);
        mFirstTone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBridge.setFirstTone(DEFAULT_TONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBridge.setFirstTone(DEFAULT_TONE);
            }
        });

        mSecondTone = (Spinner)v.findViewById(R.id.bridgeSecondToneKey);
        mSecondTone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBridge.setSecondTone(DEFAULT_TONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBridge.setSecondTone(DEFAULT_TONE);
            }
        });

        mCallOrder = (Spinner)v.findViewById(R.id.callOrderSpinner);
        mCallOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBridge.setCallOrder(DEFAULT_ORDER);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBridge.setCallOrder(DEFAULT_ORDER);
            }
        });

        return v;
    }

    public static BridgeFragment newInstance(UUID bridgeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BRIDGE_ID, bridgeId);

        BridgeFragment fragment = new BridgeFragment();
        fragment.setArguments(args);

        return fragment;
    }


}
