package com.misczak.joinmybridge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by misczak on 3/5/15.
 */
public class CallPickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.call_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

}
