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

/**
 * Created by misczak on 3/11/15.
 */
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
