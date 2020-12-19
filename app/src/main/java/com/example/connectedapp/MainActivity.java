package com.example.connectedapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    TextView tvError;
    private ProgressBar mLoading;
    private RecyclerView rvBooks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoading = findViewById(R.id.pb_loading);
        rvBooks = findViewById(R.id.rv_books);
        LinearLayoutManager bookLayout = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        rvBooks.setLayoutManager(bookLayout);

//        URL bookUrl = ApiUtils.buildUrl("cooking");
//        new BookQueryTask().execute(bookUrl);

//        try {
//            URL bookUrl = ApiUtils.buildUrl("cooking");
//            new BookQueryTask().execute(bookUrl);
//        }
//        catch (Exception e){
//
//            Log.d("error", e.getMessage() );
//        }


        //Getting the intent and putExtra from the SearchActivity
        Intent intent = getIntent();
        String query = intent.getStringExtra("QUERY");
        Log.d("MainActivity", "query value: " + query);
        URL Url;

        try {
            if (query== null || query.isEmpty()){

                Url = ApiUtils.buildUrl("cooking");
            } else {

                Url = new URL(query);
            }

            new BookQueryTask().execute(Url);

        }catch (Exception e){

            Log.d("error", e.getMessage());
        }

  }

    @Override
    public boolean onQueryTextSubmit(String query) {

        try {
            URL url = ApiUtils.buildUrl(query);
            new BookQueryTask().execute(url);

        }catch (Exception e){

            Log.d("error", e.getMessage());

        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
//            tvResult = findViewById(R.id.tvResponse);
            tvError = findViewById(R.id.tv_error);
            mLoading.setVisibility(View.INVISIBLE);
            if (result==null){
                tvError.setVisibility(View.VISIBLE);
                rvBooks.setVisibility(View.INVISIBLE);
            }else {

                tvError.setVisibility(View.INVISIBLE);
                rvBooks.setVisibility(View.VISIBLE);

                ArrayList<Books> books = ApiUtils.getBooksFromJson(result);

                BooksAdapter adapter = new BooksAdapter(books);
                rvBooks.setAdapter(adapter);
            }
//


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.book_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.bookSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case (R.id.advanceSearch):
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}