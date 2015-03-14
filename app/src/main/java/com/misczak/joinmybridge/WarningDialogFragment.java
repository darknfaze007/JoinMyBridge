package com.misczak.joinmybridge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by misczak on 3/14/15.
 */
public class WarningDialogFragment extends DialogFragment {

    public static WarningDialogFragment newInstance() {
        WarningDialogFragment fragment = new WarningDialogFragment();
        return fragment;


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.call_picker_title)
                .setNegativeButton(android.R.string.ok, null)
                .create();
    }
}


