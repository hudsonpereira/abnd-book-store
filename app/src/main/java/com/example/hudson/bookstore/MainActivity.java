package com.example.hudson.bookstore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class MainActivity extends AppCompatActivity {

    public static final int DEMO_BOOK_PRICE = 10;
    public static final int DEMO_BOOK_QUANTITY = 3;
    private ListView booksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button insertButton = findViewById(R.id.insert_data_button);
        booksListView = findViewById(R.id.book_list_View);
        booksListView.setEmptyView(findViewById(R.id.empty_text_view));

        loadBooks();

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

                loadBooks();
            }
        });
    }

    void loadBooks() {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };

        Cursor cursor = getContentResolver().query(BookContract.CONTENT_URI, projection, null, null, null);

        booksListView.setAdapter(new BookCursorAdapter(MainActivity.this, cursor));
    }

}
