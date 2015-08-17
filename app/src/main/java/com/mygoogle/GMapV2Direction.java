package com.mygoogle;

/**
 * Created by CHIP on 15/08/2015.
 */
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dichungtaxi.Utils.Util;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.util.Log;

public class GMapV2Direction {
    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";


    JSONObject jsonObj;
    public GMapV2Direction(LatLng start, LatLng end) {


        String url = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=driving";


        StringBuilder jsonResults = Util.getContentFromUrl(url.toString());
        try {
            jsonObj = new JSONObject(jsonResults.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LatLng> getDirection() {

        ArrayList<LatLng> resultList = new ArrayList<LatLng>();
        try {

            // Create a JSON object hierarchy from the results

            JSONArray steps = getLeg().getJSONArray("steps");
            for(int i = 0; i < steps.length(); i++) {
                JSONObject jLatLng = steps.getJSONObject(i);

                JSONObject jStart = jLatLng.getJSONObject("start_location");

                String lat = jStart.getString("lat");
                String lng = jStart.getString("lng");
                resultList.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

                JSONObject eStart = jLatLng.getJSONObject("end_location");
                lat = eStart.getString("lat");
                lng = eStart.getString("lng");
                resultList.add(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

            }
        } catch (JSONException e) {
            Log.e("getDirection", "Cannot process JSON results", e);
        }
        return resultList;
    }

    private JSONObject getLeg() {
        try {
            return jsonObj.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getMet() {
        try {
            return getLeg().getJSONObject("distance").getInt("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public String getDuration() {
        try {
            return getLeg().getJSONObject("duration").getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }
}