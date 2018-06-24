package com.example.hudson.bookstore.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.hudson.bookstore.R;
import com.example.hudson.bookstore.data.BookContract;

public class BookCursorAdapter extends CursorAdapter{
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context)
                .inflate(R.layout.book_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView bookTitleTextView = view.findViewById(R.id.book_title);

        String bookTitle = cursor.getString(cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRODUCT_NAME));

        bookTitleTextView.setText(bookTitle);
    }
}
