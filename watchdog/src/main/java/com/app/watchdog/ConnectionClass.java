package com.app.watchdog;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ashish John on 21/2/19.
 */
public class ConnectionClass extends AsyncTask<String, Void, Void> {


    private String postData;
    private static final String API_URL = "https://watchdog.finoit.com/api/watchdog/app-info/";

    public ConnectionClass(String postData) {
        this.postData = postData;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            // This is getting the url from the string we passed in
            URL url = new URL(API_URL);

            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("api_key", strings[0]);
            urlConnection.setRequestProperty("platform", "Android");

            urlConnection.setRequestMethod("POST");

            // Send the post body
            if (!TextUtils.isEmpty(postData)) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData);
                writer.flush();
            }

            int statusCode = urlConnection.getResponseCode();
            System.out.println("statusCode~ " + statusCode);

            if (statusCode == 200) {

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                String response = convertInputStreamToString(inputStream);

                // From here you can convert the string to JSON with whatever JSON parser you like to use
                // After converting the string to JSON, I call my custom callback. You can follow this process too,
                // or you can implement the onPostExecute(Result) method
            } else {
                // Status code is not 200
                // Do something to handle the error
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}