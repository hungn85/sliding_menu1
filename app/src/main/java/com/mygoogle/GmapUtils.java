package com.mygoogle;

import android.util.Log;

import com.dichungtaxi.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by CHIP on 15/08/2015.
 */
public class GmapUtils {
    private static final String LOG_TAG = "Google Places Autocomplete";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place/textsearch/json";

    private static final String API_KEY = "AIzaSyAAUqGKN8eVVQaw0NP2sYyTLfkCpiJOCak";

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;
        input += ", Việt Nam";
        StringBuilder url = new StringBuilder(PLACES_API_BASE );
        url.append("?key=" + API_KEY);
        url.append("&components=country:vn");
        try {
            url.append("&query=" + URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder jsonResults = Util.getContentFromUrl(url.toString());


        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
}
