package com.example.saini.maproute;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataParser {
    public String[] parseDirections(String jsonData){

        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        } catch (JSONException e) {
            Log.e("bvshi","Data Parser exception"+e.toString());
            e.printStackTrace();
        }

        return getPaths(jsonArray);
    }



    public String[] getPaths(JSONArray googleStepsJson) {
        int count = googleStepsJson.length();
        String[] polylines = new String[count];

        for(int i=0;i<googleStepsJson.length();i++) {
            try {
                polylines[i] = getPath(googleStepsJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return polylines;
    }

    public String getPath(JSONObject googlePathJson) {
        String polyline = "";
        try {
            polyline = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polyline;
    }
}
