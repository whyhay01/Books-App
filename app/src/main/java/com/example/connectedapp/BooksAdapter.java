package com.example.connectedapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder>  {

    ArrayList<Books> books;

    public BooksAdapter(ArrayList<Books> books){
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).
                inflate(R.layout.book_list_items, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Books books1 = books.get(position);
        holder.bind(books1);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtAuthor;
        TextView txtPublisher;
        TextView txtPublishedDate;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.tvTitle);
            txtAuthor = itemView.findViewById(R.id.tvAuthor);
            txtPublisher = itemView.findViewById(R.id.tvPublisher);
            txtPublishedDate = itemView.findViewById(R.id.tvPublishedDate);

        }

        public void bind (Books books){
            txtTitle.setText(books.title);
            String authors= "";
            int i = 0;
            for (String author : books.authors) {

                authors += author;
                i++;

                if (i<books.authors.length){
                    authors+=" , ";
                }
            }
            txtAuthor.setText(authors);
            txtPublisher.setText(books.publisher);
            txtPublishedDate.setText(books.publishedDate);
        }
    }
}
