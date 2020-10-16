package com.example.connectedapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        URL bookUrl = ApiUtils.buildUrl("android");
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
            tvResult.setText(result);
        }
    }
}