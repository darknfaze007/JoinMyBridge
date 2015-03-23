package com.misczak.joinmybridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by misczak on 3/8/15.
 */
public class CallDialogFragment extends DialogFragment {

    private static final String EXTRA_CALL_OPTIONS = "call_options";
    private static final String EXTRA_BRIDGE_ID = "bridge_id";
    private final boolean[] options = new boolean[2];
    private final int participantCodeIndex = 0;
    private final int hostCodeIndex = 1;
    private UUID mBridgeId;
    private Bridge mBridge;


    public static CallDialogFragment newInstance(UUID bridgeId) {
        CallDialogFragment fragment = new CallDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BRIDGE_ID, bridgeId);
        fragment.setArguments(args);

        return fragment;
    }


     @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

         options[participantCodeIndex] = false;
         options[hostCodeIndex] = false;

         mBridgeId = (UUID)getArguments().getSerializable(EXTRA_BRIDGE_ID);

         mBridge = PhoneBook.get(getActivity()).getBridge(mBridgeId);

         if (!mBridge.getParticipantCode().equals(BridgeFragment.DEFAULT_FIELD)
                 & mBridge.getHostCode().equals(BridgeFragment.DEFAULT_FIELD)){

             return new AlertDialog.Builder(getActivity())
                     .setTitle(R.string.call_picker_title)
                     .setMultiChoiceItems(R.array.participant_only_call_options, null,
                             new DialogInterface.OnMultiChoiceClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                     options[which] = isChecked;
                                 }
                             })
                     .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int id) {
                             options[hostCodeIndex] = false;
                             sendResult(Activity.RESULT_OK, mBridgeId);
                         }
                     })
                     .setNegativeButton(android.R.string.cancel, null)
                     .create();

         }

         else if (mBridge.getParticipantCode().equals(BridgeFragment.DEFAULT_FIELD)
                 & !mBridge.getHostCode().equals(BridgeFragment.DEFAULT_FIELD)){

             return new AlertDialog.Builder(getActivity())
                     .setTitle(R.string.call_picker_title)
                     .setMultiChoiceItems(R.array.host_only_call_options, null,
                             new DialogInterface.OnMultiChoiceClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                     options[which] = isChecked;
                                 }
                             })
                     .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int id) {
                             options[hostCodeIndex] = options[0];
                             options[participantCodeIndex] = false;
                             sendResult(Activity.RESULT_OK, mBridgeId);
                         }
                     })
                     .setNegativeButton(android.R.string.cancel, null)
                     .create();

         } else {

                return new AlertDialog.Builder(getActivity())
                     .setTitle(R.string.call_picker_title)
                     .setMultiChoiceItems(R.array.call_options, null,
                             new DialogInterface.OnMultiChoiceClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                     options[which] = isChecked;
                                 }
                             })
                     .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int id) {
                             sendResult(Activity.RESULT_OK, mBridgeId);
                         }
                     })
                     .setNegativeButton(android.R.string.cancel, null)
                     .create();

                }


     }

    private void sendResult(int resultCode, UUID bridgeId){
        if (getTargetFragment() == null) {
            return;
        }

        Log.d("JOHNZZZ", "sendResult arr: " + Arrays.toString(options));
        Intent i = new Intent();
        i.putExtra(EXTRA_CALL_OPTIONS, options);
        i.putExtra(EXTRA_BRIDGE_ID, bridgeId);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

    }
}
