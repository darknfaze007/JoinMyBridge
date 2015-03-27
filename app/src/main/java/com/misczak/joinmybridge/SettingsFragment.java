package com.misczak.joinmybridge;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

/**
 * Created by misczak on 3/26/15.
 */
public class SettingsFragment extends PreferenceFragment {

    static final String PREFERENCE_DIALER = "pref_dialer";

    SwitchPreference dialerPreference;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();

        return fragment;
    }

}
