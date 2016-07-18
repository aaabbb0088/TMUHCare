package com.tmu.tonychuang.tmuhcare.Z_Other.JSON;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by TonyChuang on 2016/6/29.
 */
public class JSONFormatter {

    public static ArrayList<String> getStringArray(JSONArray jsonArray) throws JSONException {
        ArrayList<String> stringArray = null;
        if (jsonArray != null) {
            int length = jsonArray.length();
            stringArray = new ArrayList<String>();
            for (int i = 0; i < length; i++) {
                stringArray.add(jsonArray.getString(i));
            }
        }
        return stringArray;
    }
}
