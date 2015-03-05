package com.misczak.joinmybridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by misczak on 3/3/15.
 */
public class PhoneBookFragment extends ListFragment {

    private ArrayList<Bridge> mBridgeList;
    private static final String TAG = "PhoneBookFragment";

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.phonebook_title);
        mBridgeList = PhoneBook.get(getActivity()).getBridges();

        BridgeAdapter adapter = new BridgeAdapter(mBridgeList);
        setListAdapter(adapter);


    }

    @Override
    public void onResume() {
        super.onResume();
        ((BridgeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        Bridge b = ((BridgeAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), BridgeActivity.class);
        i.putExtra(BridgeFragment.EXTRA_BRIDGE_ID, b.getBridgeId());
        startActivity(i);
    }

    private class BridgeAdapter extends ArrayAdapter<Bridge> {

        public BridgeAdapter(ArrayList<Bridge> bridgeList) {
            super(getActivity(), 0, bridgeList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_bridge, null);
            }

            Bridge b = getItem(position);

            TextView bridgeNameView = (TextView)convertView.findViewById(R.id.bridge_card_name);
            bridgeNameView.setText(b.getBridgeName());

            TextView bridgeNumberView = (TextView)convertView.findViewById(R.id.bridge_card_number);
            bridgeNumberView.setText(b.getBridgeNumber());

            TextView bridgeHostCodeView = (TextView)convertView.findViewById(R.id.bridge_card_hostCode);
            bridgeHostCodeView.setText(b.getHostCode());

            TextView bridgeParticipantCodeView = (TextView)convertView.findViewById(R.id.bridge_card_participantCode);
            bridgeParticipantCodeView.setText(b.getParticipantCode());

            return convertView;
        }


    }



}
