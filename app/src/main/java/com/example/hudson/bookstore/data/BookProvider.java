package com.example.hudson.bookstore.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.hudson.bookstore.data.BookContract.BookEntry;

import java.util.Arrays;

import static com.example.hudson.bookstore.data.BookContract.CONTENT_AUTHORITY;
import static com.example.hudson.bookstore.data.BookContract.PATH_BOOKS;

public class BookProvider extends ContentProvider {

    public static final String TAG = BookProvider.class.getSimpleName();

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int BOOKS = 100;
    public static final int BOOK_ID = 101;

    private BookDbHelper bookDbHelper;

    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_BOOKS, BOOKS);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_BOOKS + "/#", BOOK_ID);
    }

    @Override
    public boolean onCreate() {
        bookDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = bookDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                Log.i(TAG, "Row count: " + cursor.getCount());
                break;
            case BOOK_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Query error with URI: " + uri.toString());
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                return BookContract.CONTENT_ITEM_TYPE;
            case BOOK_ID:
                return BookContract.CONTENT_LIST_TYPE;
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insert error with URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = bookDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        switch (match) {
            case BOOK_ID:
                int records = database.delete(BookEntry.TABLE_NAME, BookEntry._ID + "=?", new String[]{String.valueOf(ContentUris.parseId(uri))});

                getContext().getContentResolver().notifyChange(uri, null);

                return records;
            default:
                throw new IllegalArgumentException("update error with URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = bookDbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        switch (match) {
            case BOOK_ID:
                Long bookId = ContentUris.parseId(uri);

                int records = database.update(BookEntry.TABLE_NAME, values, BookEntry._ID + "=?", new String[] {bookId.toString()});

                getContext().getContentResolver().notifyChange(uri, null);

                return records;
            default:
                throw new IllegalArgumentException("update error with URI: " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues values) {
        SQLiteDatabase database = bookDbHelper.getWritableDatabase();

        long id = database.insert(BookEntry.TABLE_NAME,
                null,
                values);

        if (id == -1) {
            Log.e(TAG, "Failed to insert row for:" + uri);
        }

        Log.i(TAG, "insertBook: Inserted with id: " + id);

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }
}
