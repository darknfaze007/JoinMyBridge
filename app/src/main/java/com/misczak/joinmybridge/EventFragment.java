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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.Calendar;
import java.util.Date;


public class EventFragment extends ListFragment
        implements android.support.v7.widget.SearchView.OnQueryTextListener,
        android.support.v7.widget.SearchView.OnCloseListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ID_COLUMN = "_id";
    private static final String TITLE_COLUMN = "title";
    private static final String LOCATION_COLUMN ="eventLocation";
    private static final String TAG = "EventFragment";
    static final String CALENDAR_ID_EXTRA = "calendarId";

    SimpleCursorAdapter mAdapter;
    SearchView mSearchView;
    long mCalendarId;
    private String filterString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Store calendar that was selected on previous screen for use in event query
        mCalendarId = (long)getArguments().getSerializable(CALENDAR_ID_EXTRA);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No events available.");

        setHasOptionsMenu(true);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.event_item,
                null,
                new String[] {CalendarContract.Events.TITLE,
                        CalendarContract.Events.EVENT_LOCATION},
                new int[] {R.id.eventTitle, R.id.eventLocation}, 0);
        setListAdapter(mAdapter);

        setListShown(false);

        getLoaderManager().initLoader(0, null, this);
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
                return true;

        }
    }

    public static class MySearchView extends SearchView {
        public MySearchView(Context context) {
            super(context);
        }

        @Override
        public void onActionViewCollapsed() {
            setQuery("", false);
            super.onActionViewCollapsed();
        }
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(R.drawable.ic_search_white_24dp);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
                MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        mSearchView = new MySearchView(getActivity());
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);
        mSearchView.setIconifiedByDefault(true);
        item.setActionView(mSearchView);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("EventFragment", "Item clicked: " + id);

        Cursor listItemCursor = ((SimpleCursorAdapter)l.getAdapter()).getCursor();

        String eventId = listItemCursor.getString(listItemCursor.getColumnIndex(ID_COLUMN));
        String eventName = listItemCursor.getString(listItemCursor.getColumnIndex(TITLE_COLUMN));
        String eventLocation = listItemCursor.getString(listItemCursor.getColumnIndex(LOCATION_COLUMN)).trim();

        if (eventName.length() > BridgeFragment.MAX_NAME_LENGTH){
            eventName = eventName.substring(0, BridgeFragment.MAX_NAME_LENGTH);
        }

        Log.d(TAG, "Event ID " + eventId);
        Log.d(TAG, "Event Title" + eventName);
        Log.d(TAG, "Event Location " + eventLocation);

        Intent i = new Intent(getActivity(), BridgeActivity.class);

        i.putExtra(BridgeFragment.EXTRA_BRIDGE_NAME, eventName);


        ImportUtilities iu = new ImportUtilities();
        String[] bridgeComponents = iu.getNumberArray(eventLocation);

        if (bridgeComponents[0] != null && !bridgeComponents[0].isEmpty()) {
            i.putExtra(BridgeFragment.EXTRA_BRIDGE_NUMBER, bridgeComponents[0]);
        }
        if (bridgeComponents[1] != null && !bridgeComponents[1].isEmpty()) {
            i.putExtra(BridgeFragment.EXTRA_PARTICIPANT_CODE, bridgeComponents[1]);
        }
        if (bridgeComponents[2] != null && !bridgeComponents[2].isEmpty()) {
            i.putExtra(BridgeFragment.EXTRA_HOST_CODE, bridgeComponents[2]);
        }
        if (bridgeComponents[3] != null && !bridgeComponents[3].isEmpty()) {
            i.putExtra(BridgeFragment.EXTRA_FIRST_TONE, bridgeComponents[3]);
        }
        if (bridgeComponents[4] != null && !bridgeComponents[4].isEmpty()) {
            i.putExtra(BridgeFragment.EXTRA_SECOND_TONE, bridgeComponents[4]);
        }

        startActivityForResult(i, 0);

    }


    //List of columns to return when querying Event Provider
    static final String[] EVENTS_SUMMARY_PROJECTION = new String[] {
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.DTSTART
    };


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = CalendarContract.Events.CONTENT_URI;


        //Set the before boundary for events to select bridges from.
        Date cDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cDate);
        long nowTime = calendar.getTimeInMillis();

        return new CursorLoader(getActivity(), baseUri,
                EVENTS_SUMMARY_PROJECTION,
                "calendar_id=" + mCalendarId + " AND (dtstart >=" + nowTime + " OR rdate >=" + nowTime + ")", null,
                CalendarContract.Events.DTSTART + " COLLATE LOCALIZED ASC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mAdapter.swapCursor(data);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mAdapter.swapCursor(null);

    }

    @Override
    public boolean onClose() {

        if (!TextUtils.isEmpty(mSearchView.getQuery())) {
            mSearchView.setQuery(null, true);
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String newFilter = !TextUtils.isEmpty(newText) ? newText : null;

        filterString = newFilter;
        mAdapter.getFilter().filter(filterString);
        mAdapter.notifyDataSetChanged();
        return true;

    }


    public static EventFragment newInstance(long calendarId) {

        Bundle args = new Bundle();
        args.putSerializable("calendarId", calendarId);
        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);

        return fragment;
    }
}