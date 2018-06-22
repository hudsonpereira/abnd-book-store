package com.example.hudson.bookstore.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.hudson.bookstore.data.BookContract.BookEntry;

public class BookDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Books.db";
    public static final int DATABASE_VERSION = 1;

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_SQL = "CREATE TABLE " + BookEntry.TABLE_NAME + "(" +
            BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BookEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL," +
            BookEntry.COLUMN_PRICE + " INTEGER NOT NULL," +
            BookEntry.COLUMN_QUANTITY + " INTEGER NOT NULL," +
            BookEntry.COLUMN_SUPPLIER_NAME + " TEXT," +
            BookEntry.COLUMN_SUPPLIER_PHONE_NUMBER + " TEXT)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME);
        onCreate(db);
    }
}
