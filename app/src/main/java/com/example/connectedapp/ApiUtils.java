package com.example.connectedapp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiUtils {
    //class will contain static methods and constant and it will never be instantiated
    //it is a good idea to remove the constructors

    private ApiUtils(){}

    //create a constant for the base url
    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes/";

    //Create and build url connection
    public static URL buildUrl (String title){
        String fullUrl = BASE_API_URL + "?q=" + title;
        //convert the string to url
        URL url = null;
        try {
            url = new URL(fullUrl);
        } catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

    //Connect to API
    public static String getJson(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = connection.getInputStream();

            //Convert the stream to a string using Scanner class
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return null;

        } finally {
            connection.disconnect();
        }
    }
}
