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

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;


public class PhoneBook {
    private static final String TAG = "PhoneBook";
    private static final String FILENAME = "phonebook.json";

    private static PhoneBook sPhoneBook;
    private ArrayList<Bridge> mBridgeList;
    private BridgeJSONSerializer mSerializer;
    private Context mContext;


    private PhoneBook(Context context) {
        mContext = context;

        mSerializer = new BridgeJSONSerializer(mContext, FILENAME);

        try {
            mBridgeList = mSerializer.loadPhoneBook();
        } catch (Exception e) {
            mBridgeList = new ArrayList<Bridge>();
            Log.e(TAG, "Error loading PhoneBook: ", e);
        }
    }

    public static PhoneBook get(Context c){
        if (sPhoneBook == null){
            sPhoneBook = new PhoneBook(c.getApplicationContext());
        }

        return sPhoneBook;
    }

    public void addBridge(Bridge b) {
        mBridgeList.add(b);
    }

    public void deleteBridge(Bridge b) { mBridgeList.remove(b); }

    public ArrayList<Bridge> getBridges(){
        return mBridgeList;
    }

    public Bridge getBridge(UUID bridgeId) {
        for (Bridge b: mBridgeList){
            if (b.getBridgeId().equals(bridgeId))
                return b;
        }

        return null;
    }


    public boolean savePhoneBook() {
        try {
            mSerializer.savePhoneBook(mBridgeList);
            Log.d(TAG, "PhoneBook saved to file");
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "Error saving PhoneBook: ", e);
            return false;
        }
    }


}
