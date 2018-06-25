package com.example.hudson.bookstore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hudson.bookstore.data.BookContract;
import com.example.hudson.bookstore.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    public static final String EXTRA_BOOK_ID = "EXTRA_BOOK_ID";

    TextInputEditText bookTitleEditText;
    TextInputEditText priceEditText;
    TextInputEditText quantityEditText;
    TextInputEditText supplierNameEditText;
    TextInputEditText supplierPhoneEditText;

    Uri bookUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        bookUri = intent.getData();

        if (bookUri != null) {
            getSupportActionBar().setTitle(R.string.edit_book);

            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_BOOK_ID, bookUri.toString());

            getSupportLoaderManager().initLoader(1, bundle, this);

        } else {
            getSupportActionBar().setTitle(R.string.add_book);
        }

        bookTitleEditText = findViewById(R.id.book_title_edit);
        priceEditText = findViewById(R.id.book_price);
        quantityEditText = findViewById(R.id.book_quantity);
        supplierNameEditText = findViewById(R.id.supplier_name);
        supplierPhoneEditText = findViewById(R.id.supplier_phone);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER
        };

        return new CursorLoader(
                this,
                Uri.parse(args.getString(EXTRA_BOOK_ID)),
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();

        bookTitleEditText.setText(data.getString(data.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME)));
        quantityEditText.setText(data.getString(data.getColumnIndex(BookEntry.COLUMN_QUANTITY)));
        priceEditText.setText(data.getString(data.getColumnIndex(BookEntry.COLUMN_PRICE)));
        supplierNameEditText.setText(data.getString(data.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME)));
        supplierPhoneEditText.setText(data.getString(data.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER)));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        bookTitleEditText.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);

        if (bookUri == null) {
            MenuItem item = menu.findItem(R.id.menu_delete);
            item.setVisible(false);
        }
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:

                ContentValues cv = new ContentValues();
                cv.put(BookEntry.COLUMN_PRODUCT_NAME, bookTitleEditText.getText().toString());
                cv.put(BookEntry.COLUMN_PRICE, priceEditText.getText().toString());
                cv.put(BookEntry.COLUMN_QUANTITY, quantityEditText.getText().toString());
                cv.put(BookEntry.COLUMN_SUPPLIER_NAME, supplierNameEditText.getText().toString());
                cv.put(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER, supplierPhoneEditText.getText().toString());

                if (bookUri != null) {

                    int updated = getContentResolver().update(bookUri, cv, null, null);

                    if (updated == 1) {
                        Toast.makeText(this, getString(R.string.books_updated_successfully), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    getContentResolver().insert(BookContract.CONTENT_URI, cv);

                    Toast.makeText(this, getString(R.string.book_inserted_successfully), Toast.LENGTH_SHORT).show();

                    onBackPressed();
                    finish();
                }

                return true;
            case R.id.menu_delete:
                getContentResolver().delete(bookUri, null, null);

                Toast.makeText(this, getString(R.string.book_deleted_successfully), Toast.LENGTH_SHORT).show();

                onBackPressed();
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
