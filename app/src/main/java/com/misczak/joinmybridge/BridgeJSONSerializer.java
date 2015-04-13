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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


public class BridgeJSONSerializer {

    private Context mContext;
    private String mFileName;


    public BridgeJSONSerializer(Context context, String file) {
        mContext = context;
        mFileName = file;
    }

    public ArrayList<Bridge> loadPhoneBook() throws IOException, JSONException {
        ArrayList<Bridge> bridgeList = new ArrayList<Bridge>();
        BufferedReader reader = null;

        try {
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < array.length(); i++){
                bridgeList.add(new Bridge(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            //Ignored for now, as it happens on first run
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return bridgeList;

    }


    public void savePhoneBook(ArrayList<Bridge> bridgeList)
            throws JSONException, IOException, FileNotFoundException {

        JSONArray array = new JSONArray();

        for (Bridge b : bridgeList) {
            array.put(b.toJSON());
        }

        Writer writer = null;

        try {
            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());

        } finally {
            if (writer != null){
                writer.close();
            }
        }

    }

}
