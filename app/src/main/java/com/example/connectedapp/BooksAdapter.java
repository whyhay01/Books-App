package com.example.connectedapp;

import android.content.Context;
import android.content.Intent;
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

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            itemView.setOnClickListener(this);

        }

        public void bind (Books books){
            txtTitle.setText(books.title);
            txtAuthor.setText(books.authors);
            txtPublisher.setText(books.publisher);
            txtPublishedDate.setText(books.publishedDate);
        }

        @Override
        public void onClick(View v) {
            //get the position of the book a user selected
            int position = getAdapterPosition();

            // We get the book that was selected
            Books selectedBook = books.get(position);

            //Create an Intent to move to the BookDetail activity
            Intent intent = new Intent(v.getContext(), BookDetails.class);

            //To transfer the data to the next activity we use the putExtra() method
            intent.putExtra("book", selectedBook);
            v.getContext().startActivity(intent);



        }
    }
}
