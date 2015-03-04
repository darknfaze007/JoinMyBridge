package com.misczak.joinmybridge;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements DrawerItemAdapter.ClickListener{


    private RecyclerView recyclerView;

    public static final String PREF_FILE_NAME="user_pref";
    public static final String DRAWER_OPENED_BEFORE="drawer_opened_before";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private DrawerItemAdapter adapter;

    private boolean mDrawerOpenedBefore;
    private boolean mFromSavedInstanceState;

    private View drawerView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerOpenedBefore = Boolean.valueOf(readFromPreferences(getActivity(),
                DRAWER_OPENED_BEFORE, "false"));

        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new DrawerItemAdapter(getActivity(), getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }


    public static List<DrawerItem> getData() {
        List<DrawerItem> items = new ArrayList<>();
        int[] icons = {R.drawable.ic_quick_contacts_dialer_white_24dp, R.drawable.ic_stars_white_24dp, R.drawable.ic_settings_white_24dp, R.drawable.ic_email_white_24dp};
        String[] names = {"PhoneBook", "Starred", "Settings", "Feedback"};

        for (int i = 0; i < names.length && i < icons.length; i++){
            DrawerItem drawerItem = new DrawerItem();

            drawerItem.setMenuItemId(icons[i]);
            drawerItem.setMenuItemTitle(names[i]);

            items.add(drawerItem);
        }

        return items;
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {

        drawerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mDrawerOpenedBefore) {
                    mDrawerOpenedBefore = true;

                    saveToPreferences(getActivity(), DRAWER_OPENED_BEFORE, mDrawerOpenedBefore + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                getActivity().invalidateOptionsMenu();
            }

        };

        if (!mDrawerOpenedBefore && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(drawerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();

    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void itemClicked(View view, int position) {
        startActivity(new Intent(getActivity(), BridgeActivity.class));
    }

}
