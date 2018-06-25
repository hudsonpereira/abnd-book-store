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
import com.example.hudson.bookstore.data.BookContract.BookEntry;

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
        TextView bookQuantityTextView = view.findViewById(R.id.book_quantity);
        TextView priceTextView = view.findViewById(R.id.price);

        bookTitleTextView.setText(cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME)));
        bookQuantityTextView.setText(cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY)));
        priceTextView.setText(cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PRICE)));
    }
}
