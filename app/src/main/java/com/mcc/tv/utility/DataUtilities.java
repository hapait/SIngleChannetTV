package com.mcc.tv.utility;

import android.content.Context;
import android.util.Log;

import com.mcc.tv.R;
import com.mcc.tv.model.Program;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class DataUtilities {
    public static String loadJSONFromAsset(Context mContext, String jsonFileName) {
        //GOES TO UTILITIES
        String json = null;
        try {
            InputStream is = mContext.getAssets().open(jsonFileName+".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public static ArrayList<Program> getProgramList(Context mContext) {
        //GOES TO UTILITIES

        ArrayList<Program> pList = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(DataUtilities.loadJSONFromAsset(mContext, "data"));
            JSONArray m_jArry = obj.getJSONArray("Program_Data");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);

                int programId = jo_inside.getInt("program_id");
                String programName = jo_inside.getString("program_name");
                String programTime = jo_inside.getString("program_time");

                Program programData = new Program(programId, programName, programTime);
                pList.add(programData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pList;
    }

}
