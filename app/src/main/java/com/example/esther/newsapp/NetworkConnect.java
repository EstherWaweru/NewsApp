package com.example.esther.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class NetworkConnect {
    private static final String LOG_TAG = NetworkConnect.class.getSimpleName();

    private NetworkConnect(){

    }
    public static List<Rugby> getRugbyNews(String requestUrl){
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of the news uri
        List<Rugby> rugbyNews = extractFeatureFromJson(jsonResponse);

        // Return the list of rugby news
        return rugbyNews;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static List<Rugby> extractFeatureFromJson(String rugbyJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(rugbyJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding news to
        List<Rugby> rugbies = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(rugbyJSON);
            //Extract a JSONObject associated with the key "response" which represents a list of responses
            JSONObject response=baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of results
            JSONArray rugbyArray = response.getJSONArray("results");

            // For each rugby news in the rugbyArray, create rugby object
            for (int i = 0; i < rugbyArray.length(); i++) {

                // Get  rugby news at position i within the list of rugby news
                JSONObject currentRugbyNews= rugbyArray.getJSONObject(i);

                // For one rugby news, extract the JSONObject


                // Extract the value for the key called "webTitle"
                String newsTitle= currentRugbyNews.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String newsSection= currentRugbyNews.getString("sectionName");

                // Extract the value for the key called "webPublicationDate"
                String publicationDate = currentRugbyNews.getString("webPublicationDate");

                // Extract the value for the key called "webUrl"
                String webUrl = currentRugbyNews.getString("webUrl");

                // Create a new rugby object with the webTitle,sectionName,webPublicationDate
                // and webUrl from the JSON response.
                Rugby rugby = new Rugby(newsTitle, newsSection, publicationDate, webUrl);

                // Add the new rugby object to the list of rugby objects.
                rugbies.add(rugby);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the rugby news JSON results", e);
        }

        // Return the list of rugby news
        return rugbies;
    }

}

