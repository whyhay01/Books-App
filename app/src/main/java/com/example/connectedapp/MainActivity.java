package com.example.connectedapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView tvResult, tvError;
    private ProgressBar mLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoading = findViewById(R.id.pb_loading);

        URL bookUrl = ApiUtils.buildUrl("cooking");
        new BookQueryTask().execute(bookUrl);

    }

    public class BookQueryTask extends AsyncTask <URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;
            try {
                result = ApiUtils.getJson(searchUrl);
            }
            catch (IOException e){
                Log.e("error", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            tvResult = findViewById(R.id.tvResponse);
            tvError = findViewById(R.id.tv_error);
            mLoading.setVisibility(View.INVISIBLE);
            if (result==null){
                tvError.setVisibility(View.VISIBLE);
                tvResult.setVisibility(View.INVISIBLE);
            }else {

                tvError.setVisibility(View.INVISIBLE);
                tvResult.setVisibility(View.VISIBLE);
            }
//
            ArrayList<Books> books = ApiUtils.getBooksFromJson(result);

            String stringResult = "";
            for (Books book : books){
                stringResult = stringResult + book.title + "\n" +
                        book.publishedDate + "\n\n" ;
            }

            tvResult.setText(stringResult);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
        }
    }
}