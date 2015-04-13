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


public class CallDialogFragment extends DialogFragment {

    private static final String TAG = "CallDialogFragment";
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

        Log.d(TAG, "sendResult arr: " + Arrays.toString(options));
        Intent i = new Intent();
        i.putExtra(EXTRA_CALL_OPTIONS, options);
        i.putExtra(EXTRA_BRIDGE_ID, bridgeId);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

    }
}
