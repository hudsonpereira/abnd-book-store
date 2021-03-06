package com.example.hudson.bookstore;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hudson.bookstore.adapter.BookCursorAdapter;
import com.example.hudson.bookstore.data.BookContract;
import com.example.hudson.bookstore.data.BookContract.BookEntry;
import com.example.hudson.bookstore.data.BookDbHelper;
import com.example.hudson.bookstore.data.BookProvider;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int CURSOR_LOADER = 1;

    private BookCursorAdapter bookCursorAdapter = new BookCursorAdapter(this, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton insertButton = findViewById(R.id.insert_data_button);
        ListView booksListView = findViewById(R.id.book_list_View);
        booksListView.setEmptyView(findViewById(R.id.empty_text_view));
        booksListView.setAdapter(bookCursorAdapter);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                intent.setData(ContentUris.withAppendedId(BookContract.CONTENT_URI, id));

                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(CURSOR_LOADER, null, this);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditorActivity.class));
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };

        switch (id) {
            case CURSOR_LOADER:
                return new CursorLoader(this,
                        BookContract.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bookCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookCursorAdapter.changeCursor(null);
    }
}
