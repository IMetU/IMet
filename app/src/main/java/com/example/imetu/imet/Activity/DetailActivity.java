package com.example.imetu.imet.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.imetu.imet.Model.Member;
import com.example.imetu.imet.R;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {
    private Member member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //  get user info
        member = (Member)Parcels.unwrap(getIntent().getParcelableExtra("member"));
    }
}
