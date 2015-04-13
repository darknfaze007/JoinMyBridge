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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class CalendarFragment extends ListFragment
        implements android.support.v7.widget.SearchView.OnQueryTextListener,
        android.support.v7.widget.SearchView.OnCloseListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter mAdapter;

    SearchView mSearchView;

    String mCurFilter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No calendars available.");

        setHasOptionsMenu(true);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.calendar_item,
                null,
                new String[] {CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        CalendarContract.Calendars.ACCOUNT_NAME},
                new int[] {R.id.calendarDisplayName, R.id.calendarAccountName}, 0);
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


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("CalendarFragment", "Item clicked: " + id);
        Log.d("CalendarFragment", "Position: " + position);
        Object calendarInfo = l.getItemAtPosition(position);
        Log.d("CalendarFragment", "Array: " + calendarInfo.toString());

        Intent eventIntent = new Intent(getActivity(), EventActivity.class);
        eventIntent.putExtra(EventFragment.CALENDAR_ID_EXTRA, id);
        startActivity(eventIntent);

    }


    static final String[] CALENDARS_SUMMARY_PROJECTION = new String[] {
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.ACCOUNT_NAME
    };


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri;
        if (mCurFilter != null) {
            baseUri = Uri.withAppendedPath(CalendarContract.Calendars.CONTENT_URI,
                    Uri.encode(mCurFilter));
        } else {
            baseUri = CalendarContract.Calendars.CONTENT_URI;
        }


        String select = "((" + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " NOTNULL))";

        return new CursorLoader(getActivity(), baseUri,
                CALENDARS_SUMMARY_PROJECTION,
                select, null,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + " COLLATE LOCALIZED ASC");

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


    public static CalendarFragment newInstance() {

        CalendarFragment fragment = new CalendarFragment();

        return fragment;
    }
}
