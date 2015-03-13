package com.misczak.joinmybridge;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by misczak on 3/3/15.
 */
public class PhoneBookFragment extends ListFragment {

    private static final String DIALOG_CALL = "call";
    private static final String EXTRA_CALL_OPTIONS = "call_options";
    private static final String EXTRA_BRIDGE_ID = "bridgeId";
    private static final int REQUEST_CALL = 0;
    private final int DIVIDER_HEIGHT = 10;

    private String phoneNumber;
    private ArrayList<Bridge> mBridgeList;
    private static final String TAG = "PhoneBookFragment";

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.phonebook_title);
        mBridgeList = PhoneBook.get(getActivity()).getBridges();

        BridgeAdapter adapter = new BridgeAdapter(mBridgeList);
        setListAdapter(adapter);


    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setDivider(null);
        getListView().setDividerHeight(DIVIDER_HEIGHT);
        getListView().setHeaderDividersEnabled(true);
        getListView().setFooterDividersEnabled(true);
        getListView().addHeaderView(new View(getActivity()));
        getListView().addFooterView(new View(getActivity()));

    }

    @Override
    public void onResume() {
        super.onResume();
        ((BridgeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_bridge_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_bridge:
                Bridge bridge = new Bridge();
                PhoneBook.get(getActivity()).addBridge(bridge);
                Intent i = new Intent(getActivity(), BridgeActivity.class);
                i.putExtra(BridgeFragment.EXTRA_BRIDGE_ID, bridge.getBridgeId());
                startActivityForResult(i, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

        //Really need to double check this. Was crashing on KitKat without the minus 1, but worked
        //fine on Lollipop emulator
        Bridge b = ((BridgeAdapter)getListAdapter()).getItem(position-1);

        /*Intent i = new Intent(getActivity(), BridgePagerActivity.class);
        i.putExtra(BridgeFragment.EXTRA_BRIDGE_ID, b.getBridgeId());
        startActivity(i);*/


        FragmentManager fm = getActivity().getSupportFragmentManager();
        CallDialogFragment dialog = CallDialogFragment.newInstance(b.getBridgeId());
        dialog.setTargetFragment(PhoneBookFragment.this, REQUEST_CALL);
        dialog.show(fm, DIALOG_CALL);
    }

    /*public void showCardOverFlowMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.bridge_card_overflow, popup.getMenu());
        popup.show();
    }*/


    //Responsible for creating menu options for overflow menu on each Bridge card
    public void showCardOverFlowMenu(View v, Bridge b) {

        final Bridge bridgeToDelete = b;

        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete_bridge:
                        String loggy2 = "Deleting bridge: " + bridgeToDelete.getBridgeName();
                        Log.d(TAG, loggy2);
                        PhoneBook.get(getActivity()).deleteBridge(bridgeToDelete);
                        ((BridgeAdapter)getListAdapter()).notifyDataSetChanged();
                        PhoneBook.get(getActivity()).savePhoneBook();
                        return true;
                    default:
                        return false;
                }
            }

        });
        popup.inflate(R.menu.bridge_card_overflow);
        popup.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        UUID bridgeId;

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_CALL) {
            boolean[] options = data.getBooleanArrayExtra(EXTRA_CALL_OPTIONS);
            bridgeId = (UUID)data.getSerializableExtra(EXTRA_BRIDGE_ID);

            Log.d("JOHNZZZ", "onActivityResult arr: " + Arrays.toString(options));


            CallUtils utils = new CallUtils();

            phoneNumber = utils.getCompleteNumber(bridgeId, mBridgeList, options[0], options[1]);
            placePhoneCall(phoneNumber);

            //dial = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
            //startActivity(dial);

            /*REPLACE ALL OF THIS, JUST FOR TESTING
            if (options[0] == true && options[1] == true) {
                Log.d("JOHNZZZ", "Call option 1");
                dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:11111" + bridgeId.toString()));
                startActivity(dial);

            }
            else if (options[0] == true && options[1] == false) {
                Log.d("JOHNZZZ", "Call option 2");
                dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:222222" + bridgeId.toString()));
                startActivity(dial);

            }
            else if (options[0] == false && options[1] == true) {
                Log.d("JOHNZZZ", "Call option 3");
                dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:333333" + bridgeId.toString()));
                startActivity(dial);

            }
            else {
                Log.d("JOHNZZZ", "Call option 4");
                dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:444444" + bridgeId.toString()));
                startActivity(dial);

            }*/

        }

    }

    private void placePhoneCall (String number) {

        Intent dial = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        startActivity(dial);
    }


    private class BridgeAdapter extends ArrayAdapter<Bridge> {

        public BridgeAdapter(ArrayList<Bridge> bridgeList) {
            super(getActivity(), 0, bridgeList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_bridge4, null);
            }

            final Bridge b = getItem(position);

            TextView bridgeNameView = (TextView)convertView.findViewById(R.id.bridge_card_name);
            bridgeNameView.setText(b.getBridgeName());

            TextView bridgeNumberView = (TextView)convertView.findViewById(R.id.bridge_card_number);
            bridgeNumberView.setText("Bridge Number: " + b.getBridgeNumber());

            TextView bridgeHostCodeView = (TextView)convertView.findViewById(R.id.bridge_card_hostCode);
            bridgeHostCodeView.setText("Host Code: " + b.getHostCode() + b.getSecondTone());

            TextView bridgeParticipantCodeView = (TextView)convertView.findViewById(R.id.bridge_card_participantCode);
            bridgeParticipantCodeView.setText("Participant Code: " + b.getParticipantCode() + b.getFirstTone());

            TextView bridgeCallOrder = (TextView)convertView.findViewById(R.id.bridge_call_order);
            bridgeCallOrder.setText("Code Order: " +b.getCallOrder());

            ImageView cardOverFlowMenu = (ImageView)convertView.findViewById(R.id.bridge_card_overflow);
            cardOverFlowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showCardOverFlowMenu(v, b);

                    final int position = getListView().getPositionForView((LinearLayout)v.getParent());


                    final Bridge b = (Bridge) getListView().getItemAtPosition(position);

                    String loggy = "Position: " + position + " Bridge: " + b.getBridgeName();
                    Log.d(TAG, loggy);
                }
            });


            /*Button callButton = (Button)convertView.findViewById(R.id.bridge_card_callButton);
            callButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    CallDialogFragment dialog = CallDialogFragment.newInstance(b.getBridgeId());
                    dialog.setTargetFragment(PhoneBookFragment.this, REQUEST_CALL);
                    dialog.show(fm, DIALOG_CALL);
                }
            });*/

            Button editButton = (Button)convertView.findViewById(R.id.bridge_card_editButton);
            editButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent i = new Intent(getActivity(), BridgeActivity.class);
                    i.putExtra(BridgeFragment.EXTRA_BRIDGE_ID, b.getBridgeId());
                    startActivity(i);

                }
            });

            Button shareButton = (Button)convertView.findViewById(R.id.bridge_card_shareButton);
            shareButton.setTag(Integer.valueOf(position));


            return convertView;
        }


    }

}
