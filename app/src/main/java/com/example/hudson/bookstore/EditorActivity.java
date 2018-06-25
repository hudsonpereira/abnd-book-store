package com.example.hudson.bookstore;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hudson.bookstore.data.BookContract;
import com.example.hudson.bookstore.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    public static final int CURSOR_LOADER = 0;

    public static final int CALL_REQUEST_CODE = 1;

    public static final String EXTRA_BOOK_ID = "EXTRA_BOOK_ID";

    TextInputEditText bookTitleEditText;
    TextInputEditText priceEditText;
    TextInputEditText quantityEditText;
    TextInputEditText supplierNameEditText;
    TextInputEditText supplierPhoneEditText;

    Button callSupplierButton;

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

            getSupportLoaderManager().initLoader(CURSOR_LOADER, bundle, this);

        } else {
            getSupportActionBar().setTitle(R.string.add_book);
        }

        bookTitleEditText = findViewById(R.id.book_title_edit);
        priceEditText = findViewById(R.id.book_price);
        quantityEditText = findViewById(R.id.book_quantity);
        supplierNameEditText = findViewById(R.id.supplier_name);
        supplierPhoneEditText = findViewById(R.id.supplier_phone);
        callSupplierButton = findViewById(R.id.call_supplier);
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
    public void onLoadFinished(@NonNull Loader<Cursor> loader, final Cursor data) {
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();

            bookTitleEditText.setText(data.getString(data.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME)));
            quantityEditText.setText(data.getString(data.getColumnIndex(BookEntry.COLUMN_QUANTITY)));
            priceEditText.setText(data.getString(data.getColumnIndex(BookEntry.COLUMN_PRICE)));
            supplierNameEditText.setText(data.getString(data.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME)));
            supplierPhoneEditText.setText(data.getString(data.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER)));

            callSupplierButton.setVisibility(View.VISIBLE);
            callSupplierButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    String number = data.getString(data.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER));

                    if (TextUtils.isEmpty(number)) {
                        Toast.makeText(EditorActivity.this, R.string.invalid_supplier_phone, Toast.LENGTH_SHORT).show();
                    }

                    intent.setData(Uri.parse("tel:" + number));

                    if (ActivityCompat.checkSelfPermission(EditorActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(EditorActivity.this, new String[] {Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);

                        return;
                    }
                    startActivity(intent);
                }
            });
        }
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

                if (TextUtils.isEmpty(cv.getAsString(BookEntry.COLUMN_PRODUCT_NAME))) {
                    Toast.makeText(this, R.string.invalid_product_name, Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (TextUtils.isEmpty(cv.getAsString(BookEntry.COLUMN_PRICE))) {
                    Toast.makeText(this, R.string.invalid_price, Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (TextUtils.isEmpty(cv.getAsString(BookEntry.COLUMN_QUANTITY)) || cv.getAsInteger(BookEntry.COLUMN_QUANTITY) < 0) {
                    Toast.makeText(this, R.string.invalid_quantity, Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (TextUtils.isEmpty(cv.getAsString(BookEntry.COLUMN_SUPPLIER_NAME))) {
                    Toast.makeText(this, R.string.invalid_supplier_name, Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (TextUtils.isEmpty(cv.getAsString(BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER))) {
                    Toast.makeText(this, R.string.invalid_supplier_phone, Toast.LENGTH_SHORT).show();
                    return true;
                }

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

                new AlertDialog.Builder(this)
                        .setMessage(R.string.confirm_delete)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getContentResolver().delete(bookUri, null, null);

                                Toast.makeText(EditorActivity.this, getString(R.string.book_deleted_successfully), Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
