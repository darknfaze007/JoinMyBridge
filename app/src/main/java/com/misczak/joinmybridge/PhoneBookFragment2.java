package com.misczak.joinmybridge;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class PhoneBookFragment2 extends Fragment {


    public static PhoneBookFragment2 newInstance() {
        PhoneBookFragment2 fragment = new PhoneBookFragment2();

        return fragment;
    }

    public PhoneBookFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.fragment_phonebook, container, false);

        EditText bridgeNumber = (EditText)v.findViewById(R.id.bridgeNumber);
        bridgeNumber.setText("18885859008");

        EditText pinCode = (EditText)v.findViewById(R.id.bridgePINCode);
        pinCode.setText("370814751");

        EditText hostCode = (EditText)v.findViewById(R.id.bridgeHostCode);
        hostCode.setText("9869976");

        Button dialButton = (Button)v.findViewById(R.id.dialButton);
        dialButton.setOnClickListener(new DialOnClickListener(bridgeNumber.getText().toString(),
                pinCode.getText().toString(), hostCode.getText().toString()));

        Button dialWithPINButton = (Button)v.findViewById(R.id.dialWithPINButton);
        dialWithPINButton.setOnClickListener(new DialOnClickListener(bridgeNumber.getText().toString(),
                pinCode.getText().toString(), hostCode.getText().toString()));

        Button dialWithHostButton = (Button)v.findViewById(R.id.dialWithHostButton);
        dialWithHostButton.setOnClickListener(new DialOnClickListener(bridgeNumber.getText().toString(),
                pinCode.getText().toString(), hostCode.getText().toString()));

        return v;

    }
    public class DialOnClickListener implements View.OnClickListener {

        String phoneNumber, pinCode, hostCode;
        Intent dial;

        public DialOnClickListener(String phoneNumber, String pinCode, String hostCode) {
            this.phoneNumber = "tel:" + phoneNumber.trim();
            this.pinCode = pinCode.trim();
            this.hostCode = hostCode.trim();
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.dialButton:
                    //Uncomment for testing regular bridge line calls
                    dial = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
                    startActivity(dial);
                    break;
                case R.id.dialWithPINButton:
                    //Uncomment for testing bridge plus participant code calls
                    phoneNumber += "," + pinCode + "%23";
                    dial = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
                    startActivity(dial);
                    break;
                case R.id.dialWithHostButton:
                    //Uncomment for testing bridge plus participant plus host code calls
                    phoneNumber += "," + pinCode + "%23" + "," + hostCode + "*";
                    dial = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
                    startActivity(dial);
                    break;
            }
        }

    }

}
