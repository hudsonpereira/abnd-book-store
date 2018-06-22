package com.example.hudson.bookstore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hudson.bookstore.data.BookContract;
import com.example.hudson.bookstore.data.BookContract.BookEntry;
import com.example.hudson.bookstore.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button insertButton = findViewById(R.id.insert_data_button);
        Button readButton = findViewById(R.id.read_data_button);
        final TextView resultsTextView = findViewById(R.id.results_text_view);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDbHelper bookDbHelper = new BookDbHelper(MainActivity.this);
                SQLiteDatabase sqLiteDatabase = bookDbHelper.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(BookEntry.COLUMN_PRODUCT_NAME, "New product");
                cv.put(BookEntry.COLUMN_PRICE, 10);
                cv.put(BookEntry.COLUMN_QUANTITY, 3);
                cv.put(BookEntry.COLUMN_SUPPLIER_NAME, "Hudson");
                cv.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, "+5514996097794");

                sqLiteDatabase.insert(BookEntry.TABLE_NAME, null, cv);

                resultsTextView.setText(getString(R.string.data_inserted));

                sqLiteDatabase.close();
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookDbHelper bookDbHelper = new BookDbHelper(MainActivity.this);
                SQLiteDatabase sqLiteDatabase = bookDbHelper.getReadableDatabase();

                String[] projection = {
                        BookEntry._ID,
                        BookEntry.COLUMN_PRODUCT_NAME,
                        BookEntry.COLUMN_PRICE,
                        BookEntry.COLUMN_QUANTITY,
                        BookEntry.COLUMN_SUPPLIER_NAME,
                        BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER
                };

                Cursor cursor = sqLiteDatabase.query(BookEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        null);

                int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
                int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
                int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
                int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
                int supplierPhoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

                StringBuilder headerText = new StringBuilder();

                for(String column: projection) {
                    headerText.append(column).append(" ");
                }

                resultsTextView.setText(headerText.append("\n\n").toString());

                while(cursor.moveToNext()) {
                    StringBuilder row = new StringBuilder();

                    int currentId = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    int currentPrice = cursor.getInt(priceColumnIndex);
                    int currentQuantity = cursor.getInt(quantityColumnIndex);
                    String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                    String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                    row.append(currentId)
                            .append(" ")
                            .append(currentName)
                            .append(" ")
                            .append(currentPrice)
                            .append(" ")
                            .append(currentQuantity)
                            .append(" ")
                            .append(currentSupplierName)
                            .append(" ")
                            .append(currentSupplierPhone);

                    resultsTextView.append(row.append("\n").toString());
                }

                cursor.close();
                sqLiteDatabase.close();
            }
        });
    }
}
