package com.example.saini.maproute;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;

public class GetDirectionsData  extends AsyncTask<Object,String,String> {
    GoogleMap mMap;
    String url;
    String googleGetDirectionsData;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleGetDirectionsData = downloadUrl.readUrl(url);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return googleGetDirectionsData;
    }


    @Override
    protected void onPostExecute(String s) {
        String [] directionList;
        DataParser parser =new DataParser();
        Log.e("bvshi","Get Directions Class"+s);
        directionList = parser.parseDirections(s);
        displayDirection(directionList);


    }

    public void displayDirection(String[] directionList) {
        int count = directionList.length;
        for(int i=0;i<count;i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.BLUE);
            options.width(10);
            options.addAll(PolyUtil.decode(directionList[i]));

            mMap.addPolyline(options);
        }
    }
}
