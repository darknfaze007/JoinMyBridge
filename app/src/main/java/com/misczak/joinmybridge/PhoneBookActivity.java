package com.misczak.joinmybridge;

import android.support.v4.app.Fragment;

/**
 * Created by misczak on 3/3/15.
 */
public class PhoneBookActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new PhoneBookFragment();
    }

}
