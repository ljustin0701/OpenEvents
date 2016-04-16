package com.example.ljust.openevents;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljust on 03/26/2016.
 */

public class EventsFetcher {

    private static final String API_KEY = BuildConfig.API_KEY; //replace with your own api key
    private static final String ONLY = "venue.city,venue.country,venue.address_1,venue.name,name,time";
    private static Uri ENDPOINT;

    private byte[] getURLBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    private String getJSONString(String urlSpec) throws IOException {
        return new String(getURLBytes(urlSpec));
    }

    public List<Event> downloadEvents(String topic) {
        List<Event> events = new ArrayList<>();
        try {
            ENDPOINT = Uri.parse("http://api.meetup.com/2/open_events?")
                .buildUpon()
                .appendQueryParameter("sign", "true")
                .appendQueryParameter("photo-host", "public")
                .appendQueryParameter("country", "us")
                .appendQueryParameter("topic", topic)
                .appendQueryParameter("city", "New York")
                .appendQueryParameter("state", "NY")
                .appendQueryParameter("page", "40")
                .appendQueryParameter("only", ONLY)
                .appendQueryParameter("key", API_KEY)
                .build();
            String jsonString = getJSONString(ENDPOINT.toString());
            JsonParser parser = new JsonParser();
            JsonObject jsonBody = (JsonObject)parser.parse(jsonString);
            parseItems(events, jsonBody);
        } catch (IOException ioe){
        } catch (JSONException je){
        }

        return events;
    }

    private void parseItems(List<Event> events, JsonObject jsonBody) throws IOException, JSONException {
        Gson gson = new Gson();
        JsonArray jsonArray = jsonBody.getAsJsonArray("results");
        for(JsonElement je : jsonArray) {
            if(!je.getAsJsonObject().has("venue")) continue;
            Event e = gson.fromJson(je, Event.class);
            events.add(e);
        }
    }

}


