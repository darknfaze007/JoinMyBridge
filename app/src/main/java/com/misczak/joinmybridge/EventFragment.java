package com.misczak.joinmybridge;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by misczak on 3/30/15.
 */
public class EventFragment extends ListFragment
        implements android.support.v7.widget.SearchView.OnQueryTextListener,
        android.support.v7.widget.SearchView.OnCloseListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ID_COLUMN = "_id";
    private static final String TITLE_COLUMN = "title";
    private static final String LOCATION_COLUMN ="eventLocation";
    private static final String TAG = "EventFragment";

    SimpleCursorAdapter mAdapter;

    SearchView mSearchView;

    String mCurFilter;

    long mCalendarId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendarId = (long)getArguments().getSerializable("calendarId");
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("EventFragment", "Item clicked: " + id);

        Cursor listItemCursor = ((SimpleCursorAdapter)((ListView)l).getAdapter()).getCursor();

        String eventId = listItemCursor.getString(listItemCursor.getColumnIndex(ID_COLUMN));
        String eventName = listItemCursor.getString(listItemCursor.getColumnIndex(TITLE_COLUMN));
        String eventLocation = listItemCursor.getString(listItemCursor.getColumnIndex(LOCATION_COLUMN));

        Log.d(TAG, "Event ID " + eventId);
        Log.d(TAG, "Event Title" + eventName);
        Log.d(TAG, "Event Location " + eventLocation);


        String delimiters = "[ ,x#*]+";
        String[] bridgeComponents = eventLocation.split(delimiters);
        int components = bridgeComponents.length;

        Intent i = new Intent(getActivity(), BridgeActivity.class);

        i.putExtra(BridgeFragment.EXTRA_BRIDGE_NAME, eventName);

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


    static final String[] EVENTS_SUMMARY_PROJECTION = new String[] {
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.EVENT_LOCATION
    };


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri;
        if (mCurFilter != null) {
            baseUri = Uri.withAppendedPath(CalendarContract.Events.CONTENT_URI,
                    Uri.encode(mCurFilter));
        } else {
            baseUri = CalendarContract.Events.CONTENT_URI;
        }


        String select = "((" + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " NOTNULL))";

        return new CursorLoader(getActivity(), baseUri,
                EVENTS_SUMMARY_PROJECTION,
                "calendar_id=" + mCalendarId, null,
                CalendarContract.Events.TITLE + " COLLATE LOCALIZED ASC");

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
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }


    public static EventFragment newInstance(long calendarId) {

        Bundle args = new Bundle();
        args.putSerializable("calendarId", calendarId);
        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);

        return fragment;
    }
}