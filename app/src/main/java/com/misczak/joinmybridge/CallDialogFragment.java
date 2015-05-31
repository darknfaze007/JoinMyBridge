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

import java.util.UUID;


public class CallDialogFragment extends DialogFragment {

    private static final String TAG = "CallDialogFragment";
    private static final String EXTRA_CALL_OPTIONS = "call_options";
    private static final String EXTRA_BRIDGE_ID = "bridge_id";
    private final boolean[] options = new boolean[2];
    private final int participantCodeIndex = 0;
    private final int hostCodeIndex = 1;
    private final int noDialOptions = 0;
    private final int participantDialOptions = 1;
    private final int hostDialOptions = 2;
    private final int bothDialOptions = 3;
    private UUID mBridgeId;
    private Bridge mBridge;
    private int mDialOptions;


    public static CallDialogFragment newInstance(UUID bridgeId) {
        CallDialogFragment fragment = new CallDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BRIDGE_ID, bridgeId);
        fragment.setArguments(args);

        return fragment;
    }


     @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


         mBridgeId = (UUID)getArguments().getSerializable(EXTRA_BRIDGE_ID);

         mBridge = PhoneBook.get(getActivity()).getBridge(mBridgeId);

         mDialOptions = mBridge.getDialOptions();

         switch (mDialOptions){
             case 0:
                 options[participantCodeIndex] = false;
                 options[hostCodeIndex] = false;
                 break;
             case 1:
                 options[participantCodeIndex] = true;
                 options[hostCodeIndex] = false;
                 break;
             case 2:
                 options[participantCodeIndex] = false;
                 options[hostCodeIndex] = true;
                 break;
             case 3:
                 options[participantCodeIndex] = true;
                 options[hostCodeIndex] = true;
                 break;
             default:
                 options[participantCodeIndex] = false;
                 options[hostCodeIndex] = false;

         }
         //Log.d(TAG, "options[0] = " + options[participantCodeIndex]);
         //Log.d(TAG, "options[1] = " + options[hostCodeIndex]);


         if (!mBridge.getParticipantCode().equals(BridgeFragment.DEFAULT_FIELD)
                 & mBridge.getHostCode().equals(BridgeFragment.DEFAULT_FIELD)){

             return new AlertDialog.Builder(getActivity())
                     .setTitle(R.string.call_picker_title)
                     .setMultiChoiceItems(R.array.participant_only_call_options, options,
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

             //Have to do some re-configuring since host code shifts to index 0 in this case
             if (options[hostCodeIndex]){
                 options[participantCodeIndex] = true;
                 options[hostCodeIndex] = false;
             }

             return new AlertDialog.Builder(getActivity())
                     .setTitle(R.string.call_picker_title)
                     .setMultiChoiceItems(R.array.host_only_call_options, options,
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
                     .setMultiChoiceItems(R.array.call_options, options,
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

        if (options[participantCodeIndex] && options[hostCodeIndex]){
            mBridge.setDialOptions(bothDialOptions);
        }
        else if (options[participantCodeIndex]) {
            mBridge.setDialOptions(participantDialOptions);
        }
        else if (options[hostCodeIndex]) {
            mBridge.setDialOptions(hostDialOptions);
        }
        else {
            mBridge.setDialOptions(noDialOptions);
        }

        PhoneBook.get(getActivity()).savePhoneBook();

        if (getTargetFragment() == null) {
            return;
        }

        //Log.d(TAG, "sendResult arr: " + Arrays.toString(options));
        Intent i = new Intent();
        i.putExtra(EXTRA_CALL_OPTIONS, options);
        i.putExtra(EXTRA_BRIDGE_ID, bridgeId);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);

    }
}
