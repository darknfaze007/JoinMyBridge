package com.misczak.joinmybridge;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.OnItemMovedListener;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by misczak on 3/3/15.
 */
public class PhoneBookFragment extends ListFragment {

    private static final String DIALOG_CALL = "call";
    private static final String EXTRA_BRIDGE_ID = "bridge_id";
    private static final String EXTRA_CALL_OPTIONS = "call_options";
    private static final String EXTRA_BRIDGE_NUMBER = "bridgeNumber";
    private static final String EXTRA_PARTICIPANT_CODE = "participantCode";
    private static final String EXTRA_HOST_CODE = "hostCode";
    private static final String SHARE_TEXT_TYPE = "text/plain";
    private static final String PAUSE_TONE = ",,";
    static final String PREFERENCE_DIALER = "pref_dialer";

    private static final int REQUEST_CALL = 0;
    private static final int REQUEST_CONTACT = 1;
    private final int DIVIDER_HEIGHT = 10;

    private String phoneNumber;
    private ArrayList<Bridge> mBridgeList;
    private static final String TAG = "PhoneBookFragment";
    private BridgeAdapter adapter;
    private SearchView searchView;
    private MenuItem searchItem;
    private String filterString;
    private DynamicListView listView;
    private AlphaInAnimationAdapter animationAdapter;
    private boolean customDialer;


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.phonebook_title);
        mBridgeList = PhoneBook.get(getActivity()).getBridges();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (DynamicListView) getActivity().findViewById(android.R.id.list);

        listView.setDivider(null);
        listView.setDividerHeight(DIVIDER_HEIGHT);
        listView.setHeaderDividersEnabled(true);
        listView.setFooterDividersEnabled(true);
        listView.addHeaderView(new View(getActivity()));
        listView.addFooterView(new View(getActivity()));

        adapter = new BridgeAdapter(mBridgeList);

        //TimedUndoAdapter timedUndoAdapter = new TimedUndoAdapter(adapter, getActivity(), new MyOnDismissCallback(adapter));
        SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(adapter, getActivity(), new MyOnDismissCallback(adapter));
        //animationAdapter = new AlphaInAnimationAdapter(timedUndoAdapter);
        animationAdapter = new AlphaInAnimationAdapter(swipeUndoAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);
        listView.enableSimpleSwipeUndo();

        /*
        listView.enableDragAndDrop();
        listView.setDraggableManager(new TouchViewDraggableManager(R.id.draganddrop_grip));
        listView.setOnItemMovedListener(new MyOnItemMovedListener(adapter));
        listView.setOnItemLongClickListener(new MyOnItemLongClickListener(listView));
        */

    }

    private static class MyOnItemLongClickListener implements AdapterView.OnItemLongClickListener {
        private final DynamicListView mListView;

        MyOnItemLongClickListener(final DynamicListView listView) {
            mListView = listView;
        }

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
            if (mListView != null) {
                mListView.startDragging(position - mListView.getHeaderViewsCount());
            }

            return true;
        }
    }

    private static class MyOnItemMovedListener implements OnItemMovedListener {
        private final BridgeAdapter mAdapter;

        private Toast mToast;

        MyOnItemMovedListener(final BridgeAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onItemMoved(final int originalPosition, final int newPosition) {
            if (mToast != null) {
                mToast.cancel();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dynamic_lv, container, false);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "PhoneBook onResume");
        adapter.notifyDataSetChanged();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        customDialer = preferences.getBoolean(PREFERENCE_DIALER, false);
        Log.d(TAG, customDialer + "");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_phonebook, menu);

        searchItem = menu.findItem(R.id.menu_item_search);
        searchView = (SearchView)searchItem.getActionView();

        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        ComponentName name = getActivity().getComponentName();
        SearchableInfo searchInfo = searchManager.getSearchableInfo(name);

        searchView.setSearchableInfo(searchInfo);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterString = s;
                adapter.getFilter().filter(s);
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_item_import:
                Intent importIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                importIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(importIntent, REQUEST_CONTACT);
                return true;
            case R.id.menu_item_settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Responsible for creating menu options for overflow menu on each Bridge card
    public void showCardOverFlowMenu(View v, Bridge b) {

        final Bridge bridgeCard = b;
        final View view = v;

        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.menu_item_modify_bridge:

                        Intent modifyIntent = new Intent(getActivity(), BridgeActivity.class);
                        modifyIntent.putExtra(BridgeFragment.EXTRA_BRIDGE_ID, bridgeCard.getBridgeId());
                        startActivity(modifyIntent);
                        return true;

                    case R.id.menu_item_export_bridge:
                        Intent exportIntent = new Intent(Intent.ACTION_INSERT);
                        exportIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                        if (!bridgeCard.getBridgeName().equals(BridgeFragment.DEFAULT_FIELD)) {
                            exportIntent.putExtra(ContactsContract.Intents.Insert.NAME, bridgeCard.getBridgeName());
                        }

                        exportIntent.putExtra(ContactsContract.Intents.Insert.PHONE, getNumberExtra(bridgeCard));
                        getActivity().startActivity(exportIntent);


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

            Log.d(TAG, " onActivityResult arr: " + Arrays.toString(options));


            CallUtilities utils = new CallUtilities();

            phoneNumber = utils.getCompleteNumber(bridgeId, mBridgeList, options[0], options[1]);
            placePhoneCall(phoneNumber);


        }

        if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();

            String[] queryFields = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                    ContactsContract.CommonDataKinds.Phone.NUMBER};

            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            if (c.getCount() == 0) {
                c.close();
                return;
            }

            c.moveToFirst();
            int phoneNumberColumn = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int displayNameColumn = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String contactPhoneNumber = c.getString(phoneNumberColumn);
            String contactDisplayName = c.getString(displayNameColumn);
            Log.d(TAG, contactDisplayName);
            Log.d(TAG, contactPhoneNumber);

            String delimiters = "[ ,x#*]+";
            String[] bridgeComponents = contactPhoneNumber.split(delimiters);
            int components = bridgeComponents.length;

            Intent i = new Intent(getActivity(), BridgeActivity.class);

            i.putExtra(BridgeFragment.EXTRA_BRIDGE_NAME, contactDisplayName);

            switch (components) {
                case 1:
                    Log.d(TAG, bridgeComponents[0]);
                    i.putExtra(BridgeFragment.EXTRA_BRIDGE_NUMBER, bridgeComponents[0]);
                    break;
                case 2:
                    Log.d(TAG, bridgeComponents[0]);
                    Log.d(TAG, bridgeComponents[1]);
                    i.putExtra(BridgeFragment.EXTRA_BRIDGE_NUMBER, bridgeComponents[0]);
                    i.putExtra(BridgeFragment.EXTRA_PARTICIPANT_CODE, bridgeComponents[1]);
                    break;
                case 3:
                    Log.d(TAG, bridgeComponents[0]);
                    Log.d(TAG, bridgeComponents[1]);
                    Log.d(TAG, bridgeComponents[2]);
                    i.putExtra(BridgeFragment.EXTRA_BRIDGE_NUMBER, bridgeComponents[0]);
                    i.putExtra(BridgeFragment.EXTRA_PARTICIPANT_CODE, bridgeComponents[1]);
                    i.putExtra(BridgeFragment.EXTRA_HOST_CODE, bridgeComponents[2]);
                    break;
                default:
                    break;
            }
            startActivityForResult(i, 0);

        }

    }

    private void placePhoneCall (String number) {

        Intent dial;

        if(customDialer == false) {
            dial = new Intent(Intent.ACTION_CALL, Uri.parse(number));
        }
        else {
            dial = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
        }

        startActivity(dial);
    }

    private void dismissBridge (Bridge b) {
        PhoneBook.get(getActivity()).deleteBridge(b);
        mBridgeList.remove(b);
        adapter.remove(b);
        adapter.notifyDataSetChanged();


        //Will remove bridge that is being deleted from search filter view, if active
        if (searchView.isShown()) {
            adapter.getFilter().filter(filterString);
        }

        PhoneBook.get(getActivity()).savePhoneBook();

    }

    private String getNumberExtra (Bridge bridgeExtra) {

        String numberExtra = "";

        if (!bridgeExtra.getParticipantCode().equals(BridgeFragment.DEFAULT_FIELD)
                && !bridgeExtra.getHostCode().equals(BridgeFragment.DEFAULT_FIELD)){
            if (bridgeExtra.getCallOrder().equals(BridgeFragment.DEFAULT_ORDER)) {
                numberExtra = bridgeExtra.getBridgeNumber()
                        + PAUSE_TONE
                        + bridgeExtra.getParticipantCode()
                        + bridgeExtra.getFirstTone()
                        + PAUSE_TONE
                        + bridgeExtra.getHostCode()
                        + bridgeExtra.getSecondTone();
            }
            else {
                numberExtra = bridgeExtra.getBridgeNumber()
                        + PAUSE_TONE
                        + bridgeExtra.getHostCode()
                        + bridgeExtra.getSecondTone()
                        + PAUSE_TONE
                        + bridgeExtra.getParticipantCode()
                        + bridgeExtra.getFirstTone();
            }
        } else if (!bridgeExtra.getParticipantCode().equals(BridgeFragment.DEFAULT_FIELD)
                && bridgeExtra.getHostCode().equals(BridgeFragment.DEFAULT_FIELD)) {

                    numberExtra = bridgeExtra.getBridgeNumber()
                        + PAUSE_TONE
                        + bridgeExtra.getParticipantCode()
                        + bridgeExtra.getFirstTone();

        } else if (bridgeExtra.getParticipantCode().equals(BridgeFragment.DEFAULT_FIELD)
                && !bridgeExtra.getHostCode().equals(BridgeFragment.DEFAULT_FIELD)) {

                    numberExtra = bridgeExtra.getBridgeNumber()
                        + PAUSE_TONE
                        + bridgeExtra.getHostCode()
                        + bridgeExtra.getSecondTone();

        }

        return numberExtra;
    }

    private class MyOnDismissCallback implements OnDismissCallback {

        private final BridgeAdapter mAdapter;

        MyOnDismissCallback(final BridgeAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                dismissBridge(mAdapter.getItem(position));
            }
        }
    }

    private class BridgeAdapter extends ArrayAdapter<Bridge> implements UndoAdapter {

        ArrayList<Bridge> mList;

        public BridgeAdapter(ArrayList<Bridge> bridgeList) {
            super(getActivity(), 0, bridgeList);

            mList = bridgeList;

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
            if (!b.getHostCode().equals(BridgeFragment.DEFAULT_FIELD)) {
                bridgeHostCodeView.setText("Host Code: " + b.getHostCode() + b.getSecondTone());
            }
            else {
                bridgeHostCodeView.setText("Host Code: " + b.getHostCode());
            }


            TextView bridgeParticipantCodeView = (TextView)convertView.findViewById(R.id.bridge_card_participantCode);

            if (!b.getParticipantCode().equals(BridgeFragment.DEFAULT_FIELD)) {
                bridgeParticipantCodeView.setText("Participant Code: " + b.getParticipantCode() + b.getFirstTone());
            }
            else {
                bridgeParticipantCodeView.setText("Participant Code: " + b.getParticipantCode());
            }



            TextView bridgeCallOrder = (TextView)convertView.findViewById(R.id.bridge_call_order);
            bridgeCallOrder.setText("Code Order: " +b.getCallOrder());

            ImageView cardOverFlowMenu = (ImageView)convertView.findViewById(R.id.bridge_card_overflow);

            cardOverFlowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showCardOverFlowMenu(v, b);

                    final int position = getListView().getPositionForView((LinearLayout)v.getParent());


                    final Bridge b = (Bridge) getListView().getItemAtPosition(position);

                    String loggy = "Overflow menu for Position: " + position + " Bridge: " + b.getBridgeName();
                    Log.d(TAG, loggy);
                }
            });


            Button callButton = (Button)convertView.findViewById(R.id.bridge_card_callButton);
            callButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (b.getParticipantCode().equals(BridgeFragment.DEFAULT_FIELD)
                            && b.getHostCode().equals(BridgeFragment.DEFAULT_FIELD)) {

                        CallUtilities utils = new CallUtilities();

                        phoneNumber = utils.getCompleteNumber(b.getBridgeId(), mBridgeList, false, false);
                        placePhoneCall(phoneNumber);

                    } else {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        CallDialogFragment dialog = CallDialogFragment.newInstance(b.getBridgeId());
                        dialog.setTargetFragment(PhoneBookFragment.this, REQUEST_CALL);
                        dialog.show(fm, DIALOG_CALL);
                    }


                }
            });

            Button shareButton = (Button)convertView.findViewById(R.id.bridge_card_shareButton);
            shareButton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType(SHARE_TEXT_TYPE);
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));

                    if (!b.getParticipantCode().equals(BridgeFragment.DEFAULT_FIELD)){
                        i.putExtra(Intent.EXTRA_TEXT, "Dial into my bridge at: " + b.getBridgeNumber() + " \n Participant Code: " + b.getParticipantCode() + b.getFirstTone());
                    }
                    else {
                        i.putExtra(Intent.EXTRA_TEXT, "Dial into my bridge at: " + b.getBridgeNumber());
                    }

                    i = Intent.createChooser(i, getString(R.string.send_bridge));
                    startActivity(i);
                }
            });

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @NonNull
        @Override
        public View getUndoView(int i, @Nullable View convertView, @NonNull ViewGroup viewGroup) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.undo_row, viewGroup, false);
            }
            return view;
        }

        @NonNull
        @Override
        public View getUndoClickView(@NonNull View view) {
            return view.findViewById(R.id.undo_row_undobutton);
        }

    }

}
