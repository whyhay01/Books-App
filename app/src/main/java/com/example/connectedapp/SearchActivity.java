package com.example.connectedapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etAuthor;
    private EditText etPublisher;
    private EditText etIsbn;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etPublisher = findViewById(R.id.etPublisher);
        etIsbn = findViewById(R.id.etIsbn);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();
                String publisher = etPublisher.getText().toString().trim();
                String ISBN = etIsbn.getText().toString().trim();

                if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && ISBN.isEmpty()){
                    String message = getString(R.string.no_entry);
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }else {
                    URL queryUrl = ApiUtils.buildUrl(title,author,publisher,ISBN);

                    Context context = getApplicationContext();
                    int position = SpUtil.getPrefInt(context, SpUtil.POSITION);

                    if (position ==0 || position == 5){
                        position = 1;
                    }else {
                        position++;
                    }
                    String key = SpUtil.QUERY + String.valueOf(position);
                    String value = title + ", "+ author + ", " + publisher + ", " + ISBN;
                    SpUtil.setPrefString(context,key,value);
                    SpUtil.setPrefInt(context, SpUtil.POSITION, position);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("QUERY", queryUrl.toString());
                    startActivity(intent);
                }
            }
        });
    }
}