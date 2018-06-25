package com.example.hudson.bookstore;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        Uri bookUri = intent.getData();

        if (bookUri != null) {
            getSupportActionBar().setTitle(R.string.edit_book);
        } else {
            getSupportActionBar().setTitle(R.string.add_book);
        }
    }
}
