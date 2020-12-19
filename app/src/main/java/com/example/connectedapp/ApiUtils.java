package com.example.connectedapp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.jar.JarException;

import static android.content.ContentValues.TAG;

public class ApiUtils {
    //class will contain static methods and constant and it will never be instantiated
    //it is a good idea to remove the constructors

    private ApiUtils(){}


    //create a constant for the base url
    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes/";

    //Create a Query parameter constant
    public static final String QUERY_PARAMETER_KEY = "q";

    //Adding google API for Unique identification
    public static final String KEY = "key";

    public static final String API_KEY = "AIzaSyAABwfjIL3lHl1FUWMSHTxSJquEoUAfwBQ";

    //create constant for the advance search tool
    public static final String TITLE = "intitle:";
    public static final String AUTHOR = "inauthor:";
    public static final String PUBLISHER = "inpublisher:";
    public static final String ISBN = "isbn:";

    //Create and build url connection
    public static URL buildUrl (String title){

        //Creating a URI Builder
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY,title)
                .appendQueryParameter(KEY,API_KEY)
                .build();


        //convert the uri to url
        URL url = null;

        try {
            url = new URL(uri.toString());

        } catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

//    Creating the advanced search and building it

    public static URL buildUrl (String title, String author, String publisher, String isbn){
        URL url = null;

        StringBuilder sb = new StringBuilder();
        if (!title.isEmpty()) sb.append(TITLE + title + "+");
        if (!author.isEmpty()) sb.append(AUTHOR + author + "+");
        if (!publisher.isEmpty()) sb.append(PUBLISHER + publisher + "+");
        if (!isbn.isEmpty()) sb.append(ISBN + isbn + "+");
        sb.setLength(sb.length()-1);
        String query = sb.toString();

        //creating a URI builder

        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, query)
                .appendQueryParameter(KEY, API_KEY)
                .build();

        try {
            url = new URL(uri.toString());

        }catch (Exception e){

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

    //Parsing JSON

    public static ArrayList<Books> getBooksFromJson(String json){
        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";
        final String DESCRIPTION = "description";
        final String IMAGE_LINKS = "imageLinks";
        final String THUMBNAIL = "thumbnail";

        ArrayList<Books> books = new ArrayList<Books>();

        try {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);
            int numOfBooks = arrayBooks.length();

            for (int i = 0; i<numOfBooks; i++){
                JSONObject bookJson = arrayBooks.getJSONObject(i);

                Log.d("getBooksFromJson", "bookJson: " + bookJson);

                JSONObject volumeInfoJson = bookJson.getJSONObject(VOLUME_INFO);
                Log.d("getBooksFromJson", "volume info Json: " + volumeInfoJson);

                JSONObject imageLinksJSon = null;

                if (volumeInfoJson.has(IMAGE_LINKS)){
                    imageLinksJSon = volumeInfoJson.getJSONObject(IMAGE_LINKS);
                }

                //Get Arraylist of authors
                int numbersOfAuthors;
                try {
                    numbersOfAuthors = volumeInfoJson.getJSONArray(AUTHORS).length();

                } catch (Exception e){
                    numbersOfAuthors = 0;
                }

                String [] authors = new String[numbersOfAuthors];
                for (int j = 0; j<numbersOfAuthors; j++){
                    authors[j] = volumeInfoJson.getJSONArray(AUTHORS).get(j).toString();
                    Log.d("getBooksFromJson", "Authors: " + authors[j]);

                }
                Books addedBooks= new Books(bookJson.getString(ID),
                        volumeInfoJson.getString(TITLE),
                        (volumeInfoJson.isNull(SUBTITLE)? "":volumeInfoJson.getString(SUBTITLE)),
                        authors,
                        (volumeInfoJson.isNull(PUBLISHER)? "":volumeInfoJson.getString(PUBLISHER)),
                        (volumeInfoJson.isNull(PUBLISHED_DATE)? "":volumeInfoJson.getString(PUBLISHED_DATE)),
                        volumeInfoJson.isNull(DESCRIPTION)? "":volumeInfoJson.getString(DESCRIPTION),
                        (imageLinksJSon==null)? "":imageLinksJSon.getString(THUMBNAIL));

                if (addedBooks == null){
                    Log.d("getBooksFromJson", "addedBooks is null ==> no book was added to Books class");
                }else {
                    Log.d("getBooksFromJson", "addedBooks has data ==> " + addedBooks);

                }



                books.add(addedBooks);
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        return books;
    }
}
