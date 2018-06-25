package com.example.hudson.bookstore.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView bookTitleTextView = view.findViewById(R.id.book_title);
        TextView bookQuantityTextView = view.findViewById(R.id.book_quantity);
        TextView priceTextView = view.findViewById(R.id.price);
        Button saleButton = view.findViewById(R.id.sale_button);
        final long id = cursor.getLong(cursor.getColumnIndex(BookEntry._ID));
        final int quantity = cursor.getInt(cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY));

        bookTitleTextView.setText(cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME)));

        priceTextView.setText(cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PRICE)));

        if (quantity > 0) {
            bookQuantityTextView.setText(context.getString(R.string.units_left, quantity));
            saleButton.setVisibility(View.VISIBLE);
        } else {
            bookQuantityTextView.setText(context.getString(R.string.no_units_left));
            saleButton.setVisibility(View.GONE);
        }

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri bookUri = ContentUris.withAppendedId(BookContract.CONTENT_URI, id);
                ContentValues values = new ContentValues();
                values.put(BookEntry.COLUMN_QUANTITY, quantity - 1);

                context.getContentResolver().update(bookUri, values, null, null);
                Toast.makeText(context, R.string.sold, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
