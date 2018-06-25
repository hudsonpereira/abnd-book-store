package com.example.hudson.bookstore;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    public static final int DEMO_BOOK_PRICE = 10;
    public static final int DEMO_BOOK_QUANTITY = 3;

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

        getLoaderManager().initLoader(CURSOR_LOADER, null, this);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(BookEntry.COLUMN_PRODUCT_NAME, getString(R.string.demo_product_name));
                cv.put(BookEntry.COLUMN_PRICE, DEMO_BOOK_PRICE);
                cv.put(BookEntry.COLUMN_QUANTITY, DEMO_BOOK_QUANTITY);
                cv.put(BookEntry.COLUMN_SUPPLIER_NAME, getString(R.string.demo_supplier_name));
                cv.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, getString(R.string.demo_supplier_phone));

                getContentResolver().insert(BookContract.CONTENT_URI, cv);
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
