package com.example.imetu.imet.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.imetu.imet.R;

/**
 * Created by kelly79126 on 2017/3/15.
 */

public class AdvanceSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search);
    }

    @Override
    public void onBackPressed() {
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra("test", "It is return from Advance backPressed");

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}
